package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.CashVoucher;
import com.leansoftwarelabs.mypis.domain.GLEntry;

import com.leansoftwarelabs.mypis.domain.GLEntryLine;

import com.leansoftwarelabs.mypis.domain.TrackingCategory;
import com.leansoftwarelabs.mypis.domain.TrackingValue;

import java.security.Principal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.Days;


@Stateless(name = "GLEntryFacade", mappedName = "MyPIS-MyPISService-GLEntryFacade")
@Local
public class GLEntryFacadeBean extends AbstractMultiTenantFacade<GLEntry> {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public GLEntryFacadeBean() {
        super(GLEntry.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CashVoucher saveCashVoucher(CashVoucher cashVoucher) throws ServiceException {
        GLEntry entry = mergeEntity(cashVoucher.getEntry());
        cashVoucher.setEntry(entry);
        return cashVoucher;
    }


    @Override
    public GLEntry mergeEntity(GLEntry entry) throws ServiceException {
        if ("Posted".equals(entry.getStatus())) {
            throw new ServiceException("This entry cannot be modified for it is already posted.");
        }
        List<GLEntryLine> forDeletion = new ArrayList<GLEntryLine>();
        for (GLEntryLine lineDetail: entry.getLineList()){
            if(lineDetail.isEmptyRow()){
                forDeletion.add(lineDetail);
            }
        }
        entry.getLineList().removeAll(forDeletion);
        for(int i = entry.getLineList().size(); i<2; i++){
            GLEntryLine lineDetail = new GLEntryLine();
            entry.addGLEntryLine(lineDetail);
        }
        if (entry.getStatus() == null) {
            entry.setStatus("Draft");
        }
        if (entry.getSerial() == null) {
            entry.setSerial(0);
        }
        return super.mergeEntity(entry);
    }


    public GLEntry postGLEntry(GLEntry entry) throws ServiceException {
        entry = mergeEntity(entry);
        if ("Posted".equals(entry.getStatus())) {
            throw new ServiceException("This entry is already posted.");
        }
        if (entry.getSerial() == null || entry.getSerial() == 0) {
            entry.setSerial(findNextSerial(entry.getTransType()));
        }
        entry.setStatus("Posted");
        return entry;
    }

    public CashVoucher postCashVoucher(CashVoucher cashVoucher) throws ServiceException {
        GLEntry entry = postGLEntry(cashVoucher.getEntry());
        cashVoucher.setEntry(entry);
        return cashVoucher;
    }

    protected Integer findNextSerial(String transType) {
        Query query = em.createNamedQuery("GLEntry.findNextSerial");
        query.setParameter("transType", transType);
        query.setParameter("tenantId", getCurrentUser().getTenant().getTenantId());
        return (Integer) query.getSingleResult();
    }


    public List<Object[]> getTrialBalanceData(Date startDate, Date endDate) {
        StringBuilder b = new StringBuilder();
        b.append("select acct_code, IF(acct_code IS NULL, 'TOTAL', acct_name) as acct_name, sum(BeginningBalance) as BeginningBalance,");
        b.append(" sum(DEBIT) as DEBIT, sum(CREDIT) as CREDIT, COALESCE(sum(BeginningBalance),0)+ COALESCE(sum(DEBIT),0) - COALESCE(sum(CREDIT),0) as EndingBalance");
        b.append(" from(select acct_code, acct_name, acct_type, sum(amount) as BeginningBalance, 0 as DEBIT, 0 as CREDIT");
        b.append(" from gl_trans_detail d inner join gl_accounts a on (d.gl_account_id=a.gl_acct_id)");
        b.append(" inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where h.tenant_id = ?2");
        b.append(" and status = 'Posted'");
        b.append(" and reporting_date < ?1 ");
        b.append(" group by acct_code, acct_name, acct_type");
        b.append(" union all");
        b.append(" select acct_code, acct_name, acct_type, 0 as BeginningBalance,");
        b.append(" sum(if(amount>0,amount,0)) as DEBIT,");
        b.append(" sum(if(amount<0,abs(amount),0)) as CREDIT");
        b.append(" from gl_trans_detail d inner join gl_accounts a on (d.gl_account_id=a.gl_acct_id)");
        b.append(" inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where h.tenant_id = ?2");
        b.append(" and status = 'Posted'");
        b.append(" and reporting_date between ?1 and ?3");
        b.append(" group by acct_code, acct_name, acct_type) tmp");
        b.append(" GROUP BY acct_code WITH ROLLUP");

        Query query = em.createNativeQuery(b.toString());
        query.setParameter(1, startDate);
        query.setParameter(2, getCurrentUser().getTenant().getTenantId());
        query.setParameter(3, endDate);
        return query.getResultList();
    }

    public List<Object[]> getBalanceSheetData(Date reportDate, Integer comparative) {
        StringBuilder b = new StringBuilder();
        b.append(" select type_group, type_sub_group, acct_type, acct_code, '' as dummy,");
        b.append(" if(type_group='1-Asset',sum(amount),-sum(amount)) as amount,");
        b.append(" if(type_group='1-Asset',sum(comparative1),-sum(comparative1)) as comparative1,");
        b.append(" if(type_group='1-Asset',sum(comparative2),-sum(comparative2)) as comparative2 from (");
        b.append(" select case type_group when '4-Nominal' then '3-Capital' else type_group end as type_group,");
        b.append(" case type_group when '4-Nominal' then 'Equity' else type_sub_group end as type_sub_group, ");
        b.append(" case type_group when '4-Nominal' then 'Retained Earnings' else acct_type end as acct_type,");
        b.append(" case type_group when '4-Nominal' then 'Unprocessed' else concat(acct_code,': ',acct_name) end as acct_code,");
        b.append(" sum(amount) as amount,");
        if (comparative > 0) {
            b.append(" sum(if(reporting_date<=date_sub(?2,INTERVAL 1 YEAR),amount,0)) as comparative1,");
        } else {
            b.append(" 0 as comparative1,");
        }
        if (comparative > 0) {
            b.append(" sum(if(reporting_date<=date_sub(?2,INTERVAL 2 YEAR),amount,0)) as comparative2");
        } else {
            b.append(" 0 as comparative2");
        }
        b.append(" from gl_trans_detail d");
        b.append(" inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)");
        b.append(" inner join gl_account_types t on (a.acct_type = t.type_name)");
        b.append(" inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where h.tenant_id = ?1");
        b.append(" and status = 'Posted'");
        b.append(" and reporting_date <= ?2");
        b.append(" group by type_group, type_sub_group, acct_type, acct_code)t");
        b.append(" group by type_group, type_sub_group, acct_type, acct_code");
        b.append(" order by type_group, type_sub_group, acct_type, acct_code");

        Query query = em.createNativeQuery(b.toString());
        int tenantId = getCurrentUser().getTenant().getTenantId();
        query.setParameter(1, tenantId);
        query.setParameter(2, reportDate);
        return query.getResultList();
    }

    public List<Object[]> getIncomeStatementData(Date startDate, Date endDate, Integer comparative, Integer unit,
                                                 String interval) {
        StringBuilder b = new StringBuilder();
        b.append(" select type_sub_group, acct_type, acct_code, gl_acct_id, case type_sub_group when 'Revenue' then -sum(amount) else sum(amount)end as amount,");
        b.append(" case type_sub_group when 'Revenue' then -sum(comparative1) else sum(comparative1) end as comparative1,");
        b.append(" case type_sub_group when 'Revenue' then -sum(comparative2) else sum(comparative2) end as comparative2 from(");
        b.append(" select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, gl_acct_id,");
        b.append(" sum(amount) as amount, 0 as comparative1, 0 as comparative2");
        appendSameIncomeStatementWhereClause(b);
        b.append(" and reporting_date between ?2 and ?3");
        b.append(" group by type_sub_group, acct_type, acct_code, index_");
        if (comparative > 0) {
            b.append(" union all select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, gl_acct_id, 0 as amount,");
            b.append(" sum(amount) as comparative1, 0 as comparative2");
            appendSameIncomeStatementWhereClause(b);
            appendSameIntervalLines(unit, interval, b);
        }
        if (comparative > 1) {
            unit = unit * 2;
            b.append(" union all select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, gl_acct_id, 0 as amount,");
            b.append(" 0 as comparative1, sum(amount) as comparative2");
            appendSameIncomeStatementWhereClause(b);
            appendSameIntervalLines(unit, interval, b);
        }
        b.append(" )v group by type_sub_group, acct_type, acct_code, gl_acct_id");
        b.append(" order by index_, acct_code");
        Query query = em.createNativeQuery(b.toString());
        int tenantId = getCurrentUser().getTenant().getTenantId();
        query.setParameter(1, tenantId);
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        return query.getResultList();
    }

    private void appendSameIntervalLines(int unit, String interval, StringBuilder b) {
        b.append(" and reporting_date between date_sub(?2, INTERVAL ");
        b.append(unit);
        b.append(" ");
        b.append(interval);
        b.append(" ) and date_sub(?3, INTERVAL ");
        b.append(unit);
        b.append(" ");
        b.append(interval);
        b.append(") group by index_, type_sub_group, acct_type, acct_code");
    }

    public void appendSameIncomeStatementWhereClause(StringBuilder b) {
        b.append(" from gl_trans_detail d");
        b.append(" inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)");
        b.append(" inner join gl_account_types t on (a.acct_type = t.type_name)");
        b.append(" inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where h.tenant_id = ?1 and status = 'Posted' and type_group = '4-Nominal'");
    }

    public List<Object[]> getIncomeStatementDataDetailedByType(Date startDate, Date endDate, Integer comparative,
                                                               Integer unit, String interval) {
        StringBuilder b = new StringBuilder();
        b.append(" select type_sub_group, acct_type, '' as dummy, '' as dummy2, case type_sub_group when 'Revenue' then -sum(amount) else sum(amount)end as amount,");
        b.append(" case type_sub_group when 'Revenue' then -sum(comparative1) else sum(comparative1) end as comparative1,");
        b.append(" case type_sub_group when 'Revenue' then -sum(comparative2) else sum(comparative2) end as comparative2 from(");
        b.append(" select index_, type_sub_group, acct_type,");
        b.append(" sum(amount) as amount, 0 as comparative1, 0 as comparative2");
        appendSameIncomeStatementWhereClause(b);
        b.append(" and reporting_date between ?2 and ?3");
        b.append(" group by type_sub_group, acct_type, acct_code, index_");
        if (comparative > 0) {
            b.append(" union all select index_, type_sub_group, acct_type, 0 as amount,");
            b.append(" sum(amount) as comparative1, 0 as comparative2");
            appendSameIncomeStatementWhereClause(b);
            appendSameIntervalLines(unit, interval, b);
        }
        if (comparative > 1) {
            unit = unit * 2;
            b.append(" union all select index_, type_sub_group, acct_type, 0 as amount,");
            b.append(" 0 as comparative1, sum(amount) as comparative2");
            appendSameIncomeStatementWhereClause(b);
            appendSameIntervalLines(unit, interval, b);
        }
        b.append(" )v group by type_sub_group, acct_type");
        b.append(" order by index_");
        Query query = em.createNativeQuery(b.toString());
        int tenantId = getCurrentUser().getTenant().getTenantId();
        query.setParameter(1, tenantId);
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        return query.getResultList();
    }

    

//    public List<Object[]> getAccountSummaryData(Integer accountId, Date startDate, Date endDate) {
//        StringBuilder b = new StringBuilder();
//        b.append(" select m.year_, m.month_, m.period, sum(DEBIT) as debit, sum(CREDIT) as credit from (");
//        b.append(" select year(h.reporting_date) as year_, month(h.reporting_date) as month_,");
//        b.append(" sum(DEBIT) as debit, sum(CREDIT) as credit");
//        b.append(" from gl_trans_detail d inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
//        b.append(" where gl_account_id = ?2");
//        b.append(" and h.tenant_id = ?1");
//        b.append(" and h.status = 'Posted'");
//        b.append(" and reporting_date between ?3 and ?4");
//        b.append(" group by year(h.reporting_date),month(h.reporting_date))v");
//        b.append(" right join (");
//        b.append(" select YEAR(adate) year_, month(aDate) as month_, date_format(aDate,'%M %Y') as period from (");
//        b.append(" select @endDate - interval (a.a + (10 * b.a) + (100 * c.a)) month as aDate from");
//        b.append(" (select 0 as a union all select 1 union all select 2 union all select 3");
//        b.append(" union all select 4 union all select 5 union all select 6 union all");
//        b.append(" select 7 union all select 8 union all select 9) a,");
//        b.append(" (select 0 as a union all select 1 union all select 2 union all select 3");
//        b.append(" union all select 4 union all select 5 union all select 6 union all");
//        b.append(" select 7 union all select 8 union all select 9) b,");
//        b.append(" (select 0 as a union all select 1 union all select 2 union all select 3");
//        b.append(" union all select 4 union all select 5 union all select 6 union all");
//        b.append(" select 7 union all select 8 union all select 9) c,");
//        b.append(" (select @startDate := ?3, @endDate := ?4) d");
//        b.append(" ) e where aDate between @startDate and @endDate");
//        b.append(" )m on (v.year_ = m.year_ and v.month_ = m.month_)");
//        b.append(" group by m.year_, m.month_, m.period;");
//        Query query = em.createNativeQuery(b.toString());
//        int tenantId = getCurrentUser().getTenant().getTenantId();
//        query.setParameter(1, tenantId);
//        query.setParameter(2, accountId);
//        query.setParameter(3, startDate);
//        query.setParameter(4, endDate);
//        return query.getResultList();
//    }

    public List<Object[]> getAccountDetailData(Integer accountId, Date startDate, Date endDate) {
        StringBuilder b = new StringBuilder();
        b.append(" select reporting_date, trans_type, voucher_serial, my_trans_ref, d.description, if(amount>0,amount,0) as DEBIT, if(amount<0,abs(amount),0) as CREDIT");
        b.append(" from gl_trans_detail d inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where gl_account_id = ?2");
        b.append(" and h.tenant_id = ?1");
        b.append(" and h.status = 'Posted'");
        b.append(" and reporting_date between ?3 and ?4");
        Query query = em.createNativeQuery(b.toString());
        int tenantId = getCurrentUser().getTenant().getTenantId();
        query.setParameter(1, tenantId);
        query.setParameter(2, accountId);
        query.setParameter(3, startDate);
        query.setParameter(4, endDate);
        return query.getResultList();
    }
    
    public List<Object[]> getAccountSummaryData(Integer accountId, String normalBalance, Date startDate, Date endDate,
                                                List<Object[]> trackingInfoList) {
        List<String[]> previousCriteriaList = buildCriteriaList(trackingInfoList);
        StringBuilder b = new StringBuilder();
        b.append(" select m.year_, m.month_, m.period, sum(DEBIT) as Debit, sum(CREDIT) as Credit, sum(Amount) as Amount");
        int colIndex = 0;
        for (String[] criteria : previousCriteriaList) {
            b.append(", sum(");
            b.append("_col"+colIndex);
            b.append(") as ");
            b.append("_col"+colIndex);
            colIndex++;
        }

        b.append(" from (select year(h.reporting_date) as year_, month(h.reporting_date) as month_,");
        b.append(" sum(if(amount>0,amount,0)) as debit, sum(if(amount<0,abs(amount),0)) as credit,");
        if("Debit".equals(normalBalance)){
            b.append(" sum(amount) as Amount");
        }else{
            b.append(" sum(amount)");
        }
        colIndex = 0;
        for (String[] criteria : previousCriteriaList) {
            if("Debit".equals(normalBalance)){
                b.append(", sum(if(");
            }else{
                b.append(", -sum(if(");
            }
            b.append(criteria[0]);
            b.append(", amount,0))");
            b.append(" as _col"+colIndex);
            colIndex++;
        }
        appendSameAccountSummaryClause(b);
        
        for (Object[] trackingInfo : trackingInfoList) {
            TrackingCategory category = (TrackingCategory) trackingInfo[0];
            String trackingField = category.getTrackingField();
            Object value = trackingInfo[1];
            if(value == null || "All".equals(value) || "".equals(value)){
                continue;
            } else if("Undefined".equals(value)){
                b.append(" AND ");
                b.append(trackingField);
                b.append(" is null");
            }else{
                b.append(" AND ");
                b.append(trackingField);
                b.append(" = ");
                b.append(value);
            }
        }
        
        b.append(" group by year(h.reporting_date),month(h.reporting_date))v");
        b.append(" right join (");
        appendPeriodFillerClause(b);
        b.append(" )m on (v.year_ = m.year_ and v.month_ = m.month_)");
        b.append(" group by m.year_, m.month_, m.period;");
        Query query = em.createNativeQuery(b.toString());
        int tenantId = 1;
        query.setParameter(1, tenantId);
        query.setParameter(2, accountId);
        query.setParameter(3, startDate);
        query.setParameter(4, endDate);
        return query.getResultList();
    }

    private void appendPeriodFillerClause(StringBuilder b) {
        b.append(" select YEAR(adate) year_, month(aDate) as month_, date_format(aDate,'%M %Y') as period from (");
        b.append(" select @endDate - interval (a.a + (10 * b.a) + (100 * c.a)) month as aDate from");
        b.append(" (select 0 as a union all select 1 union all select 2 union all select 3");
        b.append(" union all select 4 union all select 5 union all select 6 union all");
        b.append(" select 7 union all select 8 union all select 9) a,");
        b.append(" (select 0 as a union all select 1 union all select 2 union all select 3");
        b.append(" union all select 4 union all select 5 union all select 6 union all");
        b.append(" select 7 union all select 8 union all select 9) b,");
        b.append(" (select 0 as a union all select 1 union all select 2 union all select 3");
        b.append(" union all select 4 union all select 5 union all select 6 union all");
        b.append(" select 7 union all select 8 union all select 9) c,");
        b.append(" (select @startDate := ?3, @endDate := ?4) d");
        b.append(" ) e where aDate between @startDate and @endDate");
    }

    public List<String[]> buildCriteriaList(List<Object[]> trackingInfoList) {
        List<String[]> previousCriteriaList = new ArrayList<String[]>();
        for (Object[] trackingInfo : trackingInfoList) {
            TrackingCategory category = (TrackingCategory) trackingInfo[0];
            String trackingField = category.getTrackingField();
            List<TrackingValue> trackingValues = category.getTrackingValues();
            Object value = trackingInfo[1];
            if ("All".equals(value)) {
                List<String[]> criteriaList = new ArrayList<String[]>();
                for (TrackingValue trackingValue : trackingValues) {
                    if (previousCriteriaList.isEmpty()) {
                        String condition = trackingField + "=" + trackingValue.getId();
                        criteriaList.add(new String[] { condition, trackingValue.getValue() });
                    } else {
                        for (String[] criteria : previousCriteriaList) {
                            StringBuilder condition = new StringBuilder(criteria[0]);
                            condition.append(" AND ");
                            condition.append(trackingField);
                            condition.append(" = ");
                            condition.append(trackingValue.getId());
                            StringBuilder alias = new StringBuilder(criteria[1]);
                            alias.append("; ");
                            alias.append(trackingValue.getValue());
                            criteriaList.add(new String[] { condition.toString(), alias.toString() });
                        }
                    }
                }
                if (previousCriteriaList.isEmpty()) {
                    criteriaList.add(new String[] { trackingField + " is null ", "No " + category.getName() });
                } else {
                    for (String[] criteria : previousCriteriaList) {
                        StringBuilder condition = new StringBuilder(criteria[0]);
                        condition.append(" AND ");
                        condition.append(trackingField);
                        condition.append(" is null");
                        StringBuilder alias = new StringBuilder(criteria[1]);
                        alias.append("; No ");
                        alias.append(category.getName());
                        criteriaList.add(new String[] { condition.toString(), alias.toString() });
                    }

                }
                previousCriteriaList = criteriaList;
            }

        }
        return previousCriteriaList;
    }

    private void appendSameAccountSummaryClause(StringBuilder b) {
        b.append(" from gl_trans_detail d inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where gl_account_id = ?2");
        b.append(" and h.tenant_id = ?1");
        b.append(" and h.status = 'Posted'");
        b.append(" and reporting_date between ?3 and ?4");
    }
    
    public List getDailyAccountBalanceByDateRange(int accountId, Date startDate, Date endDate) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT datefield, IFNULL(AMOUNT,0) AS AMOUNT, @RUN := @RUN + IFNULL(AMOUNT,0) AS RUNNING_TOTAL");
        builder.append(" FROM (SELECT @RUN := 0) vars, calendar c LEFT JOIN (");
        builder.append(" SELECT ?1 as TRANS_DATE, sum(AMOUNT) AS AMOUNT");
        builder.append(" FROM gl_trans_detail d, gl_trans_header h");
        builder.append(" WHERE d.gl_trans_header_id = h.gl_trans_header_id");
        builder.append(" AND d.gl_account_id = ?2");
        builder.append(" AND TRANS_DATE <= ?1");
        builder.append(" UNION ALL");
        builder.append(" SELECT TRANS_DATE, SUM(AMOUNT) AS AMOUNT");
        builder.append(" FROM gl_trans_detail d, gl_trans_header h");
        builder.append(" WHERE d.gl_trans_header_id = h.gl_trans_header_id");
        builder.append(" AND d.gl_account_id = ?2");//4
        builder.append(" AND TRANS_DATE > ?2");//5
        builder.append(" AND TRANS_DATE <= ?3");//6
        builder.append(" GROUP BY TRANS_DATE) t ON (t.trans_date= c.datefield)");
        builder.append(" WHERE c.datefield between ?1 AND ?3");//7//8
        builder.append(" ORDER BY c.datefield");
        Query query = em.createNativeQuery(builder.toString());
        java.sql.Date startDateParam = new java.sql.Date(startDate.getTime());
        java.sql.Date endDateParam = new java.sql.Date(endDate.getTime());
        query.setParameter(1, startDateParam);
        query.setParameter(2, accountId);
//        query.setParameter(3, startDateParam);
//        query.setParameter(4, accountId);
//        query.setParameter(5, startDateParam);
        query.setParameter(3, endDateParam);
//        query.setParameter(7, startDateParam);
//        query.setParameter(8, endDateParam);
        List result = query.getResultList();
        System.out.println("result " + result);
        return result;
    }
}

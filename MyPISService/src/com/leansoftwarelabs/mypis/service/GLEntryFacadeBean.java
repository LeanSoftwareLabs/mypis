package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.GLEntry;

import java.security.Principal;

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


    @Override
    public GLEntry mergeEntity(GLEntry entry) throws ServiceException {
        if ("Posted".equals(entry.getStatus())) {
            throw new ServiceException("This entry cannot be modified for it is already posted.");
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
        b.append(" from(select acct_code, acct_name, acct_type, COALESCE(sum(debit),0)-COALESCE(sum(credit),0) as BeginningBalance, 0 as DEBIT, 0 as CREDIT");
        b.append(" from gl_trans_detail d inner join gl_accounts a on (d.gl_account_id=a.gl_acct_id)");
        b.append(" inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where h.tenant_id = ?2");
        b.append(" and status = 'Posted'");
        b.append(" and reporting_date < ?1 ");
        b.append(" group by acct_code, acct_name, acct_type");
        b.append(" union all");
        b.append(" select acct_code, acct_name, acct_type, 0 as BeginningBalance,");
        b.append(" sum(debit) as DEBIT,");
        b.append(" sum(credit) as CREDIT");
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
        b.append(" coalesce(sum(debit),0) - coalesce(sum(credit),0) as amount,");
        if (comparative > 0) {
            b.append(" coalesce(sum(if(reporting_date<=date_sub(?2,INTERVAL 1 YEAR),debit,0)),0)- coalesce(sum(if(reporting_date<=date_sub(?2,INTERVAL 1 YEAR),credit,0)),0) as comparative1,");
        } else {
            b.append(" 0 as comparative1,");
        }
        if (comparative > 0) {
            b.append(" coalesce(sum(if(reporting_date<=date_sub(?2,INTERVAL 2 YEAR),debit,0)),0)- coalesce(sum(if(reporting_date<=date_sub(?2,INTERVAL 2 YEAR),credit,0)),0) as comparative2");
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
        b.append(" coalesce(sum(debit),0)- coalesce(sum(credit),0) as amount, 0 as comparative1, 0 as comparative2");
        appendSameIncomeStatementWhereClause(b);
        b.append(" and reporting_date between ?2 and ?3");
        b.append(" group by type_sub_group, acct_type, acct_code, index_");
        if (comparative > 0) {
            b.append(" union all select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, gl_acct_id, 0 as amount,");
            b.append(" coalesce(sum(debit),0)- coalesce(sum(credit),0) as comparative1, 0 as comparative2");
            appendSameIncomeStatementWhereClause(b);
            appendSameIntervalLines(unit, interval, b);
        }
        if (comparative > 1) {
            unit = unit * 2;
            b.append(" union all select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, gl_acct_id, 0 as amount,");
            b.append(" coalesce(sum(debit),0)- coalesce(sum(credit),0) as comparative1, 0 as comparative2");
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
        b.append(" coalesce(sum(debit),0)- coalesce(sum(credit),0) as amount, 0 as comparative1, 0 as comparative2");
        appendSameIncomeStatementWhereClause(b);
        b.append(" and reporting_date between ?2 and ?3");
        b.append(" group by type_sub_group, acct_type, acct_code, index_");
        if (comparative > 0) {
            b.append(" union all select index_, type_sub_group, acct_type, 0 as amount,");
            b.append(" coalesce(sum(debit),0)- coalesce(sum(credit),0) as comparative1, 0 as comparative2");
            appendSameIncomeStatementWhereClause(b);
            appendSameIntervalLines(unit, interval, b);
        }
        if (comparative > 1) {
            unit = unit * 2;
            b.append(" union all select index_, type_sub_group, acct_type, 0 as amount,");
            b.append(" coalesce(sum(debit),0)- coalesce(sum(credit),0) as comparative1, 0 as comparative2");
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
    
    public List<Object[]> getAccountSummaryData(Integer accountId, Date startDate, Date endDate){
        StringBuilder b = new StringBuilder();
        b.append(" select m.year_, m.month_, m.period, sum(DEBIT) as debit, sum(CREDIT) as credit from (");
        b.append(" select year(h.reporting_date) as year_, month(h.reporting_date) as month_,");
        b.append(" sum(DEBIT) as debit, sum(CREDIT) as credit");
        b.append(" from gl_trans_detail d inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)");
        b.append(" where gl_account_id = ?2");
        b.append(" and h.tenant_id = ?1");
        b.append(" and h.status = 'Posted'");
        b.append(" and reporting_date between ?3 and ?4");
        b.append(" group by year(h.reporting_date),month(h.reporting_date))v");
        b.append(" right join (");
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
        b.append(" )m on (v.year_ = m.year_ and v.month_ = m.month_)");
        b.append(" group by m.year_, m.month_, m.period;");
        Query query = em.createNativeQuery(b.toString());
        int tenantId = getCurrentUser().getTenant().getTenantId();
        query.setParameter(1, tenantId);
        query.setParameter(2, accountId);
        query.setParameter(3, startDate);
        query.setParameter(4, endDate);
        return query.getResultList();
    }
    
    public List<Object[]> getAccountDetailData(Integer accountId, Date startDate, Date endDate){
        StringBuilder b = new StringBuilder();
        b.append(" select reporting_date, trans_type, voucher_serial, my_trans_ref, d.description, DEBIT, CREDIT");
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
}

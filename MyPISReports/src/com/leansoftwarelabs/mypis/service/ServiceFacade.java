package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.domain.ConfirmationRegister;

import com.leansoftwarelabs.mypis.domain.TrackingCategory;
import com.leansoftwarelabs.mypis.domain.TrackingValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.poi.hssf.record.formula.functions.T;

public class ServiceFacade {
    private final EntityManager em;

    public ServiceFacade() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("OutsideContainer");
        em = emf.createEntityManager();

    }

    /**
     * All changes that have been made to the managed entities in the
     * persistence context are applied to the database and committed.
     */
    private void commitTransaction() {
        final EntityTransaction entityTransaction = em.getTransaction();
        if (!entityTransaction.isActive()) {
            entityTransaction.begin();
        }
        entityTransaction.commit();
    }

    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    /** <code>Select o from ConfirmationRegister o where o.registerId = :registerId</code> */
    public List<ConfirmationRegister> getFindConfirmationRegisterById(Integer registerId) {
        return em.createNamedQuery("findConfirmationRegisterById",
                                   ConfirmationRegister.class).setParameter("registerId", registerId).getResultList();
    }

    /** <code>Select o from BaptismalRegister o where o.registerId = :registerId</code> */
    public List<BaptismalRegister> getFindBaptismalRegisterById(Integer registerId) {
        return em.createNamedQuery("findBaptismalRegisterById", BaptismalRegister.class).setParameter("registerId",
                                                                                                      registerId).getResultList();
    }

    public List findAll(Class entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root root = cq.from(entityClass);
        Path path = root.get("tenantId");
        Predicate predicate = cb.equal(path, 1);
        cq.where(predicate);
        return (List) em.createQuery(cq).getResultList();
    }

    public List<Object[]> getAccountSummaryData(Integer accountId, Date startDate, Date endDate,
                                                List<Object[]> trackingInfoList) {
        List<String[]> previousCriteriaList = buildCriteriaList(trackingInfoList);
        StringBuilder b = new StringBuilder();
        b.append(" select m.year_, m.month_, m.period, sum(DEBIT) as debit, sum(CREDIT) as credit");

        for (String[] criteria : previousCriteriaList) {
            b.append(", sum(");
            String alias = criteria[1].replaceAll("\\s+", "");
            b.append(alias);
            b.append(") as ");
            b.append(alias);
        }

        b.append(" from (select year(h.reporting_date) as year_, month(h.reporting_date) as month_,");
        b.append(" sum(DEBIT) as debit, sum(CREDIT) as credit");
        for (String[] criteria : previousCriteriaList) {
            b.append(", sum(if(");
            b.append(criteria[0]);
            b.append(", debit,0)) as ");
            String alias = criteria[1].replaceAll("\\s+", "");
            b.append(alias);
        }
        appendSameAccountSummaryClause(b);
        
        for (Object[] trackingInfo : trackingInfoList) {
            TrackingCategory category = (TrackingCategory) trackingInfo[0];
            String trackingField = category.getTrackingField();
            Object value = trackingInfo[1];
            if(value == null || "All".equals(value)){
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
        List<String[]> previousCriteriaList = null;
        for (Object[] trackingInfo : trackingInfoList) {
            TrackingCategory category = (TrackingCategory) trackingInfo[0];
            String trackingField = category.getTrackingField();
            List<TrackingValue> trackingValues = category.getTrackingValues();
            Object value = trackingInfo[1];
            if ("All".equals(value)) {
                List<String[]> criteriaList = new ArrayList<String[]>();
                for (TrackingValue trackingValue : trackingValues) {
                    if (previousCriteriaList == null) {
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
                            alias.append("_");
                            alias.append(trackingValue.getValue());
                            criteriaList.add(new String[] { condition.toString(), alias.toString() });
                        }
                    }
                }
                if (previousCriteriaList == null) {
                    criteriaList.add(new String[] { trackingField + " is null ", "Undefined_" + category.getName() });
                } else {
                    for (String[] criteria : previousCriteriaList) {
                        StringBuilder condition = new StringBuilder(criteria[0]);
                        condition.append(" AND ");
                        condition.append(trackingField);
                        condition.append(" is null");
                        StringBuilder alias = new StringBuilder(criteria[1]);
                        alias.append("_Undefined_");
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


}

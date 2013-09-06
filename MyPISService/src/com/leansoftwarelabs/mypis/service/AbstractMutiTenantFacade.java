package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.MultiTenant;
import com.leansoftwarelabs.realm.domain.User;

import java.security.Principal;

import java.util.Collections;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public abstract class AbstractMutiTenantFacade<T extends MultiTenant> {
    @Inject
    Principal principal;
    private Class<T> entityClass;

    public AbstractMutiTenantFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return (List<T>) getEntityManager().createQuery(cq).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Collections.emptyList();
        }
        Integer tenantId = currentUser.getTenant().getTenantId();
        StringBuffer buffer = new StringBuffer(jpqlStmt);
        int index = buffer.toString().toUpperCase().indexOf("ORDER BY");
        String tenantFilterClause = containsWhereClause(jpqlStmt)?" AND o.tenantId = :tenantId ": " WHERE o.tenantId = :tenantId ";
        if(index == -1){
            buffer.append(tenantFilterClause);
        }else{
            buffer.insert(index, tenantFilterClause);
        }
        Query query = getEntityManager().createQuery(buffer.toString());
        query.setParameter("tenantId", tenantId);
        setFirstAndMaxResults(query, firstResult, maxResults);
        return query.getResultList();
    }

    private User getCurrentUser() {
        User currentUser = getEntityManager().find(User.class, principal.getName());
        return currentUser;
    }

    protected void setFirstAndMaxResults(Query query, int firstResult, int maxResults) {
        if (firstResult > 0) {
            query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
    }

    protected void appendInitialConjuction(StringBuffer buffer) {
        if (buffer.toString().toUpperCase().contains("WHERE")) {
            buffer.append(" AND");
        } else {
            buffer.append(" WHERE");
        }
    }
    
    protected boolean containsWhereClause(String jpql) {
        return jpql.toUpperCase().contains("WHERE");
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T persistEntity(T entity) {
        User currentUser = getCurrentUser();
        if(entity.getTenantId() == null){
            entity.setTenantId(currentUser.getTenant().getTenantId());
        }
        getEntityManager().persist(entity);
        return entity;
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public T mergeEntity(T entity) {
        User currentUser = getCurrentUser();
        if(entity.getTenantId() == null){
            entity.setTenantId(currentUser.getTenant().getTenantId());
        }
        entity = getEntityManager().merge(entity);
        return entity;
    }
}

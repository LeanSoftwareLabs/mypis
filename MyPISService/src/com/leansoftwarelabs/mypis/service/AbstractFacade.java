package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.realm.domain.User;

import java.security.Principal;

import java.util.Collections;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class AbstractFacade {
    @Inject
    Principal principal;
    
    protected abstract EntityManager getEntityManager();
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults) {
        User currentUser = getEntityManager().find(User.class, principal.getName());
        if (currentUser == null) {
            return Collections.emptyList();
        }
        Integer tenantId = currentUser.getTenant().getTenantId();
        StringBuffer buffer = new StringBuffer(jpqlStmt);
        appendInitialConjuction(buffer);
        buffer.append(" o.tenantId = :tenantId");
        Query query = getEntityManager().createQuery(buffer.toString());
        query.setParameter("tenantId", tenantId);
        setFirstAndMaxResults(query, firstResult, maxResults);
        return query.getResultList();
    }

    private void setFirstAndMaxResults(Query query, int firstResult, int maxResults) {
        if (firstResult > 0) {
            query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
    }

    private void appendInitialConjuction(StringBuffer buffer) {
        if (buffer.toString().toUpperCase().contains("WHERE")) {
            buffer.append(" AND");
        } else {
            buffer.append(" WHERE");
        }
    }
    
    public <T> T persistEntity(T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        return getEntityManager().merge(entity);
    }
}

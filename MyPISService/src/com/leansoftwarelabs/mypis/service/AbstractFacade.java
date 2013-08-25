package com.leansoftwarelabs.mypis.service;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class AbstractFacade {
    protected abstract EntityManager getEntityManager();
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults) {
        Query query = getEntityManager().createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }
    
    public <T> T persistEntity(T entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        return getEntityManager().merge(entity);
    }
}

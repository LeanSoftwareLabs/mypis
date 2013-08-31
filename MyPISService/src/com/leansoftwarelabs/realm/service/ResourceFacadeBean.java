package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.mypis.service.AbstractFacade;

import com.leansoftwarelabs.realm.domain.Resource;

import java.util.List;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ResourceFacade", mappedName = "MyPIS-MyPISService-ResourceFacade")
public class ResourceFacadeBean extends AbstractFacade<Resource>{
    @javax.annotation.Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "JDBCRealm")
    private EntityManager em;

    public ResourceFacadeBean() {
        super(Resource.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /** <code>select o from Resource o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Resource> getAllResource() {
        return em.createNamedQuery("Resource.findAll", Resource.class).getResultList();
    }
}

package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Tenant;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "TenantFacade", mappedName = "MyPIS-MyPISService-TenantFacade")
public class TenantFacadeBean extends AbstractFacade<Tenant>{
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "JDBCRealm")
    private EntityManager em;

    public TenantFacadeBean() {
        super(Tenant.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}

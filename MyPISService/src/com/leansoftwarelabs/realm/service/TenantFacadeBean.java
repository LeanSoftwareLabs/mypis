package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Tenant;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "TenantFacade", mappedName = "MyPIS-MyPISService-TenantFacade")
public class TenantFacadeBean implements TenantFacade, TenantFacadeLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "JDBCRealm")
    private EntityManager em;

    public TenantFacadeBean() {
    }

    public Tenant mergeTenant(Tenant tenant) {
        return em.merge(tenant);
    }

    public void removeTenant(Tenant tenant) {
        tenant = em.find(Tenant.class, tenant.getTenantId());
        em.remove(tenant);
    }
    
    public Tenant findTenantById(Integer tenantId){
        return em.find(Tenant.class, tenantId);
    }
}

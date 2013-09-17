package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.SubAccount;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "SubAccountFacade", mappedName = "MyPIS-MyPISService-SubAccountFacade")
@Local
public class SubAccountFacadeBean extends AbstractMultiTenantFacade<SubAccount>{
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public SubAccountFacadeBean() {
        super(SubAccount.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}

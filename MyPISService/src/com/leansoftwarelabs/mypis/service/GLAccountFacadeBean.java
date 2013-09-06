package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.GLAccount;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "GLAccountFacade", mappedName = "MyPIS-MyPISService-GLAccountFacade")
@Local
public class GLAccountFacadeBean extends AbstractMutiTenantFacade<GLAccount>{
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public GLAccountFacadeBean() {
        super(GLAccount.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}

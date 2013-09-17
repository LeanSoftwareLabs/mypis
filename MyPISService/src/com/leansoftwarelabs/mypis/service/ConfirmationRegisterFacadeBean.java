package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.domain.ConfirmationRegister;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ConfirmationRegisterFacade", mappedName = "MyPIS-MyPISService-ConfirmationRegisterFacade")
public class ConfirmationRegisterFacadeBean extends AbstractMultiTenantFacade<ConfirmationRegister>{
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public ConfirmationRegisterFacadeBean() {
        super(ConfirmationRegister.class);
    }

    public void removeBaptismalRegister(BaptismalRegister baptismalRegister) {
        baptismalRegister = em.find(BaptismalRegister.class, baptismalRegister.getRegisterId());
        em.remove(baptismalRegister);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}

package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
@Local
@Stateless(name = "BaptismalRegisterFacade", mappedName = "MyPIS-MyPISService-BaptismalRegisterFacade")
public class BaptismalRegisterFacadeBean extends AbstractFacade<BaptismalRegister>{
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public BaptismalRegisterFacadeBean() {
        super(BaptismalRegister.class);
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BaptismalRegister findBaptismalRegisterById(Integer registerId) {
        Query query = em.createNamedQuery("findBaptismalRegisterById");
        query.setParameter("registerId", registerId);
        return (BaptismalRegister) query.getSingleResult();
    }
}

package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.Contact;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ContactFacade", mappedName = "MyPIS-MyPISService-ContactFacade")
@Local
public class ContactFacadeBean extends AbstractFacade<Contact>{
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public ContactFacadeBean() {
        super(Contact.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}

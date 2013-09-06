package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.GLEntry;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "GLEntryFacade", mappedName = "MyPIS-MyPISService-GLEntryFacade")
@Local
public class GLEntryFacadeBean extends AbstractMutiTenantFacade<GLEntry> {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public GLEntryFacadeBean() {
        super(GLEntry.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    } 
    

}

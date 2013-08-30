package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.domain.ConfirmationRegister;

import java.util.List;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class ServiceFacade {
    private final EntityManager em;

    public ServiceFacade() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("OutsideContainer");
        em = emf.createEntityManager();
      
    }

    /**
     * All changes that have been made to the managed entities in the
     * persistence context are applied to the database and committed.
     */
    private void commitTransaction() {
        final EntityTransaction entityTransaction = em.getTransaction();
        if (!entityTransaction.isActive()) {
            entityTransaction.begin();
        }
        entityTransaction.commit();
    }

    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    /** <code>Select o from ConfirmationRegister o where o.registerId = :registerId</code> */
    public List<ConfirmationRegister> getFindConfirmationRegisterById(Integer registerId) {
        return em.createNamedQuery("findConfirmationRegisterById",
                                   ConfirmationRegister.class).setParameter("registerId", registerId).getResultList();
    }

    /** <code>Select o from BaptismalRegister o where o.registerId = :registerId</code> */
    public List<BaptismalRegister> getFindBaptismalRegisterById(Integer registerId) {
        return em.createNamedQuery("findBaptismalRegisterById", BaptismalRegister.class).setParameter("registerId",
                                                                                                      registerId).getResultList();
    }
}

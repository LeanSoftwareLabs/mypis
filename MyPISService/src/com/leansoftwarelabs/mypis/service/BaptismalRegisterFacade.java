package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface BaptismalRegisterFacade {
    Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults);

    <T> T persistEntity(T entity);

    <T> T mergeEntity(T entity);

    void removeBaptismalRegister(BaptismalRegister baptismalRegister);

    BaptismalRegister findBaptismalRegisterById(Integer registerId);
}

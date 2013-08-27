package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;

import com.leansoftwarelabs.mypis.domain.ConfirmationRegister;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ConfirmationRegisterFacadeLocal {
    Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults);

    <T> T persistEntity(T entity);

    <T> T mergeEntity(T entity);

    void removeBaptismalRegister(BaptismalRegister baptismalRegister);

   ConfirmationRegister findConfirmationRegisterById(Integer registerId);
}

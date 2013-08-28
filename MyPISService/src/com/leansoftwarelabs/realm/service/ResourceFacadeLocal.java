package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Resource;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ResourceFacadeLocal {
    Object queryByRange(String jpqlStmt, List<Object[]> hints, int firstResult, int maxResults);

    List<Resource> getAllResource();
}

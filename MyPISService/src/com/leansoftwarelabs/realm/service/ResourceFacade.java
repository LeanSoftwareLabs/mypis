package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Resource;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ResourceFacade {
    List<Resource> getAllResource();
}

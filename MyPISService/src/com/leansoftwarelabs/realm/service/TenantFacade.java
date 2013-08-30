package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Tenant;

import javax.ejb.Remote;

@Remote
public interface TenantFacade {
    Tenant mergeTenant(Tenant tenant);

    void removeTenant(Tenant tenant);
}

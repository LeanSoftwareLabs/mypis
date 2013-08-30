package com.leansoftwarelabs.realm.service;

import com.leansoftwarelabs.realm.domain.Tenant;

import javax.ejb.Local;

@Local
public interface TenantFacadeLocal {
    Tenant mergeTenant(Tenant tenant);

    void removeTenant(Tenant tenant);
    
    Tenant findTenantById(Integer tenantId);
}

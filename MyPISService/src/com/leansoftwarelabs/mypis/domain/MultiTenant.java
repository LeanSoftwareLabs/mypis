package com.leansoftwarelabs.mypis.domain;

public interface MultiTenant {
    Integer getTenantId();
    void setTenantId(Integer tenantId);
}

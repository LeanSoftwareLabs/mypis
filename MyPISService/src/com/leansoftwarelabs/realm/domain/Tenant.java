package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "\"tenants\"")
public class Tenant implements Serializable {
    private static final long serialVersionUID = -3299480164188882488L;
    @Column(name = "tenant_description")
    private String description;
    @Id
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    public Tenant() {
    }

    public Tenant(String description, Integer tenantId) {
        this.description = description;
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}

package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SubAccount.findAll", query = "select o from SubAccount o") })
@Table(name = "\"subsidiary_accounts\"")
public class SubAccount implements MultiTenant, Serializable {
    private static final long serialVersionUID = 6100546466848836578L;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "description")
    private String description;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "subsidiary_account_id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "tenant_id", nullable = false, unique = true)
    private Integer tenantId;

    public SubAccount() {
    }

    public SubAccount(String code, String description, Integer id, String name, Integer tenantId) {
        this.code = code;
        this.description = description;
        this.id = id;
        this.name = name;
        this.tenantId = tenantId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}

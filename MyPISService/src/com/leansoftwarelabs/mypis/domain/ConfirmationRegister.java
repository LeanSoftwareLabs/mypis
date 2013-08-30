package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQueries({ @NamedQuery(name = "findConfirmationRegisterById", query = "Select o from ConfirmationRegister o where o.registerId = :registerId")
    })
@Entity
@Table(name = "confirmation_register")
public class ConfirmationRegister implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CONFIRMATION_REGISTER_ID", nullable = false)
    private Integer registerId;
    @Column(nullable = false)
    private Integer line;
    @Column(nullable = false)
    private Integer volume;
    @Column(nullable = false)
    private Integer page;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(min = 2, max = 45)
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @NotNull
    @Column(name = "TENANT_ID", nullable = false)
    private Integer tenantId;


    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getTenantId() {
        return tenantId;
    }


    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    public Integer getRegisterId() {
        return registerId;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getLine() {
        return line;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public ConfirmationRegister() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

}

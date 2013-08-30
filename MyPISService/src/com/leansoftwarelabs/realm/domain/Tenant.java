package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    @Column(name = "DISPLAY_NAME")
    private String displayName;
    @Column(name = "LEGAL_NAME")
    private String legalName;
    @Column(name = "ORG_TYPE")
    private String orgType;
    @Column(name = "BUSINESS_LINE")
    private String businessLine;
    @Column(name = "TELEPHONE")
    private String telephone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ATTENTION_TO")
    private String attentionTo;
    @Column(name = "ADDRESS_LINE1")
    private String addressLine1;
    @Column(name = "ADDRESS_LINE2")
    private String addressLine2;
    @Column(name = "CITY")
    private String city;
    @Column(name = "REGION")
    private String region;
    @Column(name = "ZIP_CODE")
    private String zipCode;
    @Column(name = "COUNTRY")
    private String country;

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


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setBusinessLine(String businessLine) {
        this.businessLine = businessLine;
    }

    public String getBusinessLine() {
        return businessLine;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAttentionTo(String attentionTo) {
        this.attentionTo = attentionTo;
    }

    public String getAttentionTo() {
        return attentionTo;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
    
    
}

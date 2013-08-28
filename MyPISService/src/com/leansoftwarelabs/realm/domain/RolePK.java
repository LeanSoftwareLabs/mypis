package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

public class RolePK implements Serializable {
    public String roleName;
    public Integer tenantId;

    public RolePK() {
    }

    public RolePK(String role_name, Integer tenant) {
        this.roleName = role_name;
        this.tenantId = tenant;
    }

    public boolean equals(Object other) {
        if (other instanceof RolePK) {
            final RolePK otherRolePK = (RolePK) other;
            final boolean areEqual = true;
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String role_name) {
        this.roleName = role_name;
    }

    public Integer getTenant() {
        return tenantId;
    }

    public void setTenant(Integer tenant) {
        this.tenantId = tenant;
    }
}

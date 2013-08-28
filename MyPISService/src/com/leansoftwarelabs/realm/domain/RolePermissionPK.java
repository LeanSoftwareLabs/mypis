package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

public class RolePermissionPK implements Serializable {
    public Integer resourceId;
    public String roleName;
    public Integer tenantId;


    public RolePermissionPK() {
    }

    public RolePermissionPK(Integer resourceId, String roleName, Integer tenantId) {
        this.resourceId = resourceId;
        this.roleName = roleName;
        this.tenantId = tenantId;
    }

    public boolean equals(Object other) {
        if (other instanceof RolePermissionPK) {
            final RolePermissionPK otherRolePermissionPK = (RolePermissionPK) other;
            final boolean areEqual = true;
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

}

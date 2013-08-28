package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.PrivateOwned;

@Entity
@NamedQueries({ @NamedQuery(name = "Role.findAll", query = "select o from Role o where o.tenantId = :tenantId") })
@Table(name = "\"roles\"")
@IdClass(RolePK.class)
public class Role implements Serializable {
    private static final long serialVersionUID = -2397080635810877057L;
    @Column(name = "role_description")
    private String description;
    @NotNull
    @Id
    @Column(name = "role_name", nullable = false)
    private String roleName;
    @NotNull
    @Id
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    @OneToMany(mappedBy = "role", cascade = {CascadeType.MERGE})
    @PrivateOwned
    private List<RolePermission> permissionList;

    public void setPermissionList(List<RolePermission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<RolePermission> getPermissionList() {
        if(permissionList == null){
            permissionList = new ArrayList<RolePermission>();
        }
        return permissionList;
    }
    
    public void addRolePermission(RolePermission permission){
        if(!permissionList.contains(permission)){
            getPermissionList().add(permission);
            permission.setRole(this);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Role)) {
            return false;
        }
        final Role other = (Role) object;
        if (!(roleName == null ? other.roleName == null : roleName.equals(other.roleName))) {
            return false;
        }
        if (!(tenantId == null ? other.tenantId == null : tenantId.equals(other.tenantId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((roleName == null) ? 0 : roleName.hashCode());
        result = PRIME * result + ((tenantId == null) ? 0 : tenantId.hashCode());
        return result;
    }

    public Role() {
    }

    public Role(String role_description, String role_name, Integer tenant) {
        this.description = role_description;
        this.roleName = role_name;
        this.tenantId = tenant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String role_description) {
        this.description = role_description;
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

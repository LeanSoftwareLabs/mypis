package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "RolePermission.findAll", query = "select o from RolePermission o") })
@Table(name = "\"roles_permissions\"")
@IdClass(RolePermissionPK.class)
public class RolePermission implements Serializable {
    private static final long serialVersionUID = -4859262547975648921L;
    @JoinColumns({ @JoinColumn(name = "role_name",
                                referencedColumnName = "role_name"),
                    @JoinColumn(name = "tenant_id",
                                referencedColumnName = "tenant_id")})
    private Role role;
    @Id
    @Column(name = "role_name", nullable = false, updatable = false, insertable = false)
    private String roleName;
    @Id
    @Column(name = "tenant_id", nullable = false, updatable = false, insertable = false)
    private Integer tenantId;
    @Id
    @Column(name = "resource_id", nullable = false, updatable = false, insertable = false)
    private Integer resourceId;
    @ManyToOne(targetEntity = Resource.class)
    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id")
    private Resource resource;
    @Column(name = "create_")
    private Boolean create;
    @Column(name = "read_")
    private Boolean read;
    @Column(name = "update_")
    private Boolean update;
    @Column(name = "delete_")
    private Boolean delete;
    
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public RolePermission() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getRead() {
        return read;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean getDelete() {
        return delete;
    }

}

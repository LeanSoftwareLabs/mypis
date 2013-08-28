package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.JoinFetch;

@Entity
@NamedQueries({ @NamedQuery(name = "User.findAll", query = "select o from User o where o.tenant = :tenant") })
@Table(name = "\"users\"")
public class User implements Serializable {
    private static final long serialVersionUID = -430291891383001299L;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "password_salt")
    private String passwordSalt;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    @NotNull
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @JoinFetch
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = {
               @JoinColumn(name = "username", referencedColumnName = "username"),
               @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
        }, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "role_name"),
            @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
        })
    private List<Role> roleList;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof User)) {
            return false;
        }
        final User other = (User) object;
        if (!(username == null ? other.username == null : username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
    

    public User() {
    }

    public User(String password, String password_salt, Tenant tenant, String username) {
        this.password = password;
        this.passwordSalt = password_salt;
        this.tenant = tenant;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String password_salt) {
        this.passwordSalt = password_salt;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

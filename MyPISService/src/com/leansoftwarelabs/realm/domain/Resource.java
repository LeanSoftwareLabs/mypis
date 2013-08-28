package com.leansoftwarelabs.realm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Resource.findAll", query = "select o from Resource o") })
@Table(name = "\"resources\"")
public class Resource implements Serializable {
    private static final long serialVersionUID = 7229272519207616826L;
    @Column(name = "resource_description")
    private String description;
    @Column(name = "resource_name", nullable = false, unique = true)
    private String name;
    @Id
    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;
    @Column(name = "resource_value", nullable = false, unique = true)
    private String value;

    public Resource() {
    }

    public Resource(String description, String name, Integer resourceId, String value) {
        this.description = description;
        this.name = name;
        this.resourceId = resourceId;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Resource)) {
            return false;
        }
        final Resource other = (Resource) object;
        if (!(name == null ? other.name == null : name.equals(other.name))) {
            return false;
        }
        if (!(value == null ? other.value == null : value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
}

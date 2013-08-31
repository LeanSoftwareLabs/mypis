package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rommel
 */
@Entity
@Table(name = "contact_groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactGroup.findAll", query = "SELECT c FROM ContactGroup c"),
    @NamedQuery(name = "ContactGroup.findByName", query = "SELECT c FROM ContactGroup c WHERE c.name = :name")})
public class ContactGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;

    public ContactGroup() {
    }

    public ContactGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactGroup)) {
            return false;
        }
        ContactGroup other = (ContactGroup) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "count.domain.ContactGroup[ name=" + name + " ]";
    }
    
    public String getStoreKey(){
        return "[" + name + "]";
    }
    
}

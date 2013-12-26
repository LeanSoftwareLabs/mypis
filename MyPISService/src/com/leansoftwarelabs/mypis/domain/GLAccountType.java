package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "GLAccountType.findAll", query = "select o from GLAccountType o order by o.index"), })
@Table(name = "\"gl_account_types\"")
public class GLAccountType implements Serializable {
    private static final long serialVersionUID = -6425519984202959968L;
    @Column(name = "type_group", nullable = false)
    private String group;
    @Column(name = "index_")
    private int index;
    @Id
    @Column(name = "type_name", nullable = false, unique = true)
    private String name;
    @Column(name = "type_sub_group", nullable = false)
    private String subGroup;
    @Column(name = "normal_balance", nullable = false)
    private String normalBalance;

    public GLAccountType() {
    }

    public GLAccountType(String group, int index, String name, String subGroup) {
        this.group = group;
        this.index = index;
        this.name = name;
        this.subGroup = subGroup;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }


    public void setNormalBalance(String normalBalance) {
        this.normalBalance = normalBalance;
    }

    public String getNormalBalance() {
        return normalBalance;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GLAccountType)) {
            return false;
        }
        final GLAccountType other = (GLAccountType) object;
        if (!(name == null ? other.name == null : name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}

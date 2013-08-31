package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rommel
 */
@Entity
@Table(name = "gl_accounts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GLAccount.findAll", query = "SELECT o FROM GLAccount o")
})
public class GLAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "acct_type")
    private String type;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "gl_acct_id")
    private Integer accountId;
    @NotNull
    @Column(name = "tenant_id")
    private Integer tenantId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "acct_code")
    private String code;
    @Size(max = 150)
    @Column(name = "acct_name")
    private String name;
    @Size(max = 200)
    @Column(name = "acct_desc")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dashboard")
    private boolean dashboard;
    @Basic(optional = false)
    @NotNull
    @Column(name = "expense_claims")
    private boolean expenseClaims;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payments")
    private boolean payments;

    public GLAccount() {
    }

    public GLAccount(Integer acctId) {
        this.accountId = acctId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public boolean getDashboard() {
        return dashboard;
    }

    public void setDashboard(boolean dashboard) {
        this.dashboard = dashboard;
    }

    public boolean getExpenseClaims() {
        return expenseClaims;
    }

    public void setExpenseClaims(boolean expenseClaims) {
        this.expenseClaims = expenseClaims;
    }

    public boolean getPayments() {
        return payments;
    }

    public void setPayments(boolean payments) {
        this.payments = payments;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountId != null ? accountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GLAccount)) {
            return false;
        }
        GLAccount other = (GLAccount) object;
        if ((this.accountId == null && other.accountId != null) || (this.accountId != null && !this.accountId.equals(other.accountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "count.entities.Account[ acctId=" + accountId + " ]";
    }
    
}

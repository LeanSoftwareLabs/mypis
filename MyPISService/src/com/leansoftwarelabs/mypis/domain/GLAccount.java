package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
    @NamedQuery(name = "GLAccount.findAll", query = "SELECT o FROM GLAccount o"),
    @NamedQuery(name = "GLAccount.findCashAndBankAccounts", query = "select o from GLAccount o where o.tenantId = :tenantId and o.type.name in('cash', 'bank')")
})
public class GLAccount implements MultiTenant, Serializable {
    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "acct_type")
    private GLAccountType type;
    @Id
    @Column(name = "gl_acct_id")
    private Integer accountId;
    @NotNull
    @Column(name = "tenant_id")
    private Integer tenantId;
    
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
    @Column(name = "dashboard")
    private boolean dashboard;
    @Column(name = "expense_claims")
    private boolean expenseClaims;
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

    public GLAccountType getType() {
        return type;
    }

    public void setType(GLAccountType type) {
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
    
//    public static final class AccountType{
//        public static final Map VALUES;
//        static {
//            VALUES = new LinkedHashMap();
//            VALUES.put("Cash", "Cash");
//            VALUES.put("Receivables", "Receivables");
//            VALUES.put("Inventories", "Inventories");
//            VALUES.put("Prepayments", "Prepayments");
//            VALUES.put("Other Current Assets", "Other Current Assets");
//            VALUES.put("Fixed Assets", "Fixed Assets");
//            VALUES.put("Payables", "Payables");
//            VALUES.put("Long-term Payables", "Long-term Payables");
//            VALUES.put("Equity", "Equity");
//            VALUES.put("Retained Earnings", "Retained Earnings");
//            VALUES.put("Revenue", "Revenue");
//            VALUES.put("Direct Cost", "Direct Cost");
//            VALUES.put("Expenses", "Expenses");
//            VALUES.put("Taxes", "Taxes");
//        }
//    }
    
}

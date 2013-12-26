package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rommel
 */
@Entity
@Table(name = "gl_trans_detail")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "GLEntryLine.findAll", query = "SELECT g FROM GLEntryLine g") })
public class GLEntryLine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GL_TRANS_DETAIL_ID")
    private Integer id;
    @NotNull
    @Column(name = "LINE_NO")
    private int lineNo;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "GL_ACCOUNT_ID")
    @ManyToOne
    private GLAccount account;

    @Column(name = "QTY")
    private Integer quantity;
    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "AMOUNT")
    private BigDecimal amount;


    @ManyToOne
    @JoinColumn(name = "GL_TRANS_HEADER_ID")
    private GLEntry entry;
    @JoinColumn(name = "SUB_ACCOUNT")
    private SubAccount subAccount;
    private Integer tracking1;
    private Integer tracking2;
    private Integer tracking3;
    private Integer tracking4;
    private Integer tracking5;

    public GLEntryLine() {
    }

    public GLEntryLine(Integer glTransDetailId) {
        this.id = glTransDetailId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GLAccount getAccount() {
        return account;
    }

    public void setAccount(GLAccount account) {
        this.account = account;
    }


    public void setDebit(BigDecimal debit) {
        if (debit != null) {
            this.amount = debit;
        }
    }

    public BigDecimal getDebit() {
        return getAmount().compareTo(BigDecimal.ZERO) > 0 ? getAmount() : null;
    }
    

    public void setCredit(BigDecimal credit) {
        if (credit != null) {
            this.amount = credit.negate();
        }
    }

    public BigDecimal getCredit() {
        return getAmount().compareTo(BigDecimal.ZERO) > 0 ? getAmount() : null;
    }

    public GLEntry getEntry() {
        return entry;
    }

    public void setEntry(GLEntry entry) {
        this.entry = entry;
    }


    public void setSubAccount(SubAccount subAccount) {
        this.subAccount = subAccount;
    }

    public SubAccount getSubAccount() {
        return subAccount;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }


    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            return;
        }
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            setDebit(amount);
        } else {
            setCredit(amount.abs());
        }
    }

    public BigDecimal getAmount() {
        if(amount == null){
            return BigDecimal.ZERO;
        }
        return amount;
    }


    public void setTracking1(Integer tracking1) {
        this.tracking1 = tracking1;
    }

    public Integer getTracking1() {
        return tracking1;
    }

    public void setTracking2(Integer tracking2) {
        this.tracking2 = tracking2;
    }

    public Integer getTracking2() {
        return tracking2;
    }

    public void setTracking3(Integer tracking3) {
        this.tracking3 = tracking3;
    }

    public Integer getTracking3() {
        return tracking3;
    }

    public void setTracking4(Integer tracking4) {
        this.tracking4 = tracking4;
    }

    public Integer getTracking4() {
        return tracking4;
    }

    public void setTracking5(Integer tracking5) {
        this.tracking5 = tracking5;
    }

    public Integer getTracking5() {
        return tracking5;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GLEntryLine)) {
            return false;
        }
        final GLEntryLine other = (GLEntryLine) object;
        if (lineNo != other.lineNo) {
            return false;
        }
        if (!(entry == null ? other.entry == null : entry.equals(other.entry))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + lineNo;
        result = PRIME * result + ((entry == null) ? 0 : entry.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "count.domain.GLEntryLine[ id=" + id + " ]";
    }

    public boolean isEmptyRow() {
        return account == null && subAccount == null && amount == null;
    }


}

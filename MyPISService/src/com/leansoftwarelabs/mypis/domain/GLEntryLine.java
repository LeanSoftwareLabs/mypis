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
@Table(name = "gl_trans_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GLEntryLine.findAll", query = "SELECT g FROM GLEntryLine g")})
public class GLEntryLine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "GL_TRANS_DETAIL_ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LINE_NO")
    private int lineNo;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "GL_ACCOUNT_ID")
    @ManyToOne
    private GLAccount account;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT")
    private double amount;
    
    @ManyToOne
    @JoinColumn(name = "GL_TRANS_HEADER_ID")
    private GLEntry entry;

    public GLEntryLine() {
    }

    public GLEntryLine(Integer glTransDetailId) {
        this.id = glTransDetailId;
    }

    public GLEntryLine(Integer glTransDetailId, int lineNo, double amount) {
        this.id = glTransDetailId;
        this.lineNo = lineNo;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public GLEntry getEntry() {
        return entry;
    }

    public void setEntry(GLEntry entry) {
        this.entry = entry;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GLEntryLine)) {
            return false;
        }
        GLEntryLine other = (GLEntryLine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "count.domain.GLEntryLine[ id=" + id + " ]";
    }
    
}

package com.leansoftwarelabs.mypis.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.PrivateOwned;

/**
 *
 * @author Rommel
 */
@Entity
@Table(name = "gl_trans_header")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GLEntry.findAll", query = "SELECT o FROM GLEntry o")})
public class GLEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "GL_TRANS_HEADER_ID")
    private Integer id;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRANS_DATE")
    @Temporal(TemporalType.DATE)
    private Date transDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REPORTING_DATE")
    @Temporal(TemporalType.DATE)
    private Date reportingDate;
    @Size(max = 10)
    @Column(name = "STATUS")
    private String status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TOTAL_DEBIT")
    private Double totalDebit;
    @Column(name = "TOTAL_CREDIT")
    private Double totalCredit;
    @Size(max = 5)
    @Column(name = "TRANS_TYPE")
    private String transType;
    @Size(max = 20)
    @Column(name = "MY_TRANS_REF")
    private String myReference;
    @Size(max = 20)
    @Column(name = "YOUR_TRANS_REF")
    private String yourReference;
    @OneToMany(mappedBy = "entry")
    @PrivateOwned
    private List<GLEntryLine> lineList;

    public GLEntry() {
    }

    public GLEntry(Integer glTransHeaderId) {
        this.id = glTransHeaderId;
    }

    public GLEntry(Integer glTransHeaderId, Date transDate, Date reportingDate) {
        this.id = glTransHeaderId;
        this.transDate = transDate;
        this.reportingDate = reportingDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getMyReference() {
        return myReference;
    }

    public void setMyReference(String myReference) {
        this.myReference = myReference;
    }

    public String getYourReference() {
        return yourReference;
    }

    public void setYourReference(String yourReference) {
        this.yourReference = yourReference;
    }
    
    

    @XmlTransient
    public List<GLEntryLine> getLineList() {
        return lineList;
    }

    public void setLineList(List<GLEntryLine> gLEntryLineList) {
        this.lineList = gLEntryLineList;
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
        if (!(object instanceof GLEntry)) {
            return false;
        }
        GLEntry other = (GLEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "count.domain.GLEntry[ id=" + id + " ]";
    }
    
}

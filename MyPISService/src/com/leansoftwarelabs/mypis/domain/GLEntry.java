package com.leansoftwarelabs.mypis.domain;

import com.leansoftwarelabs.mypis.validator.AssertMethodAsTrue;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.PrivateOwned;

/**
 *
 * @author Rommel
 */
//@AssertMethodAsTrue("areGLEntryLinesBalance")

@Entity
@Table(name = "gl_trans_header")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GLEntry.findNextSerial", query = "SELECT max(o.serial)+1 FROM GLEntry o WHERE o.tenantId = :tenantId and o.transType = :transType")})
public class GLEntry implements MultiTenant, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "GL_TRANS_HEADER_ID")
    private Integer id;
    @Column(name = "VOUCHER_SERIAL")
    private Integer serial;
        
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @NotNull
    @Column(name = "TRANS_DATE")
    @Temporal(TemporalType.DATE)
    private Date transDate;
    @NotNull
    @Column(name = "REPORTING_DATE")
    @Temporal(TemporalType.DATE)
    private Date reportingDate;
    @Size(max = 10)
    @Column(name = "STATUS")
    private String status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TOTAL_DEBIT")
    private BigDecimal totalDebit;
    @Column(name = "TOTAL_CREDIT")
    private BigDecimal totalCredit;
    @NotNull
    @Size(max = 5)
    @Column(name = "TRANS_TYPE")
    private String transType;
    @NotNull
    @Size(max = 20)
    @Column(name = "MY_TRANS_REF")
    private String myReference;

    @Column(name = "TENANT_ID")
    private Integer tenantId;
    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
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
    
    public void addGLEntryLine(GLEntryLine entryLine){
        getLineList().add(entryLine);
        entryLine.setEntry(this);   
    }
    
    public void removeGLEntryLine(GLEntryLine entryLine){
        getLineList().remove(entryLine);
        entryLine.setEntry(null);   
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Integer getSerial() {
        return serial;
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

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
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

  
    

    @XmlTransient
    public List<GLEntryLine> getLineList() {
        if(lineList == null){
            lineList = new ArrayList<GLEntryLine>();
        }
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

    @Override
    public Integer getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public BigDecimal getComputedDebit(){
        BigDecimal result = BigDecimal.ZERO;
        for (GLEntryLine line : getLineList()){
            result = result.add(line.getDebit());
        }
        return result;
    }
            
    public BigDecimal getComputedCredit(){
        BigDecimal result = BigDecimal.ZERO;
        for (GLEntryLine line : getLineList()){
            result = result.add(line.getCredit());
        }
        return result;
    }        
    
    @AssertTrue(message = "Accounting Lines not Balance")
    public boolean isGLEntryLinesBalance(){
        return getComputedDebit().compareTo(getComputedCredit()) == 0;
    }
}

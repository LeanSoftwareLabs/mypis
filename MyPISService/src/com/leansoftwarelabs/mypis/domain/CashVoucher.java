package com.leansoftwarelabs.mypis.domain;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CashVoucher {
    private GLEntry entry;
    List<GLEntryLine> lineDetails;

    public CashVoucher(GLEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("GL Entry cannot be null");
        }
        this.entry = entry;
    }

    public Integer getId() {
        return entry.getId();
    }


    public void setEntry(GLEntry entry) {
        this.entry = entry;
    }

    public GLEntry getEntry() {
        return entry;
    }

    public SubAccount getSubAccount() {
        return getFirstLine().getSubAccount();
    }

    public void setSubAccount(SubAccount subAccount) {
        getFirstLine().setSubAccount(subAccount);
    }
    
    public GLAccount getBankCashAccount(){
        return getFirstLine().getAccount();
    }
    
    public void setBankCashAccount(GLAccount account){
        getFirstLine().setAccount(account);
    }

    public GLEntryLine getFirstLine() {
        if (entry.getLineList().isEmpty()) {
            GLEntryLine lineDetail = new GLEntryLine();
            lineDetail.setLineNo(0);
            entry.addGLEntryLine(lineDetail);
        }
        return entry.getLineList().get(0);
    }

    public void addLineDetail(GLEntryLine lineDetail) {
        entry.addGLEntryLine(lineDetail);
        getLineDetails().add(lineDetail);
    }

    public void removeLineDetail(int index) {
        entry.removeGLEntryLine(index + 1); // is line excluded in table display
        getLineDetails().remove(index);
    }

    public List<GLEntryLine> getLineDetails() {
        if (lineDetails == null) {
            lineDetails = new ArrayList<GLEntryLine>();
            //entry should have at least one line detail
            if (entry.getLineList().isEmpty()) {
                GLEntryLine lineDetail = new GLEntryLine();
                lineDetail.setLineNo(0);
                entry.addGLEntryLine(lineDetail);
            }
            lineDetails.addAll(entry.getLineList());
            lineDetails.remove(0);
        }
        return lineDetails;
    }
    
    public BigDecimal getTotal(){
        BigDecimal result = BigDecimal.ZERO;
        for(GLEntryLine lineDetail: getLineDetails()){
            result = result.add(lineDetail.getAmount());
        }
        getFirstLine().setCredit(result);
        return result;
    }

    public void setSerial(Integer serial) {
        entry.setSerial(serial);
    }

    public Integer getSerial() {
        return entry.getSerial();
    }

    public String getDescription() {
        return entry.getDescription();
    }

    public void setDescription(String description) {
        entry.setDescription(description);
        getFirstLine().setDescription(description);
    }

    public Date getTransDate() {
        return entry.getTransDate();
    }

    public void setTransDate(Date transDate) {
        entry.setTransDate(transDate);
    }

    public Date getReportingDate() {
        return entry.getReportingDate();
    }

    public void setReportingDate(Date reportingDate) {
        entry.setReportingDate(reportingDate);
    }

    public String getStatus() {
        return entry.getStatus();
    }

    public void setStatus(String status) {
        entry.setStatus(status);
    }


    public String getTransType() {
        return entry.getTransType();
    }

    public void setTransType(String transType) {
        entry.setTransType(transType);
    }

    public String getMyReference() {
        return entry.getMyReference();
    }

    public void setMyReference(String myReference) {
        entry.setMyReference(myReference);
    }
}

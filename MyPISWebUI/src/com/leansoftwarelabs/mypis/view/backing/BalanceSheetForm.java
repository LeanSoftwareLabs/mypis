package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

public class BalanceSheetForm {
    private GLEntryFacadeBean service;
    private List<Object[]> data;
    private Date reportDate;
    private Integer reportLevel = 2;
    private Integer comparative = 0;

    @PostConstruct
    public void init(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        reportDate = cal.getTime();
    }


    public void setReportLevel(Integer reportLevel) {
        this.reportLevel = reportLevel;
    }

    public Integer getReportLevel() {
        return reportLevel;
    }

    public void setComparative(Integer comparative) {
        this.comparative = comparative;
    }

    public Integer getComparative() {
        return comparative;
    }

    public GLEntryFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service = (GLEntryFacadeBean) context.lookup("java:comp/env/ejb/local/GLEntryFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }
    
    public void runReport(ActionEvent actionEvent) {
        List<Object[]> resultList = getService().getBalanceSheetData(reportDate, comparative);
        System.out.println("result size: "+resultList.size());
        data = new ArrayList<Object[]>();
        String latestGroup = "";
        String latestSubGroup = "";
        String latestType= "";
        Object[] emptyRow = new Object[]{"","","","",null};
        BigDecimal totalLiabilitiesAndCapital  = BigDecimal.ZERO;
        BigDecimal groupTotal = BigDecimal.ZERO;
        BigDecimal subGroupTotal = BigDecimal.ZERO;
        BigDecimal typeTotal = BigDecimal.ZERO;
        //comparative1
        BigDecimal comparative1LC  = BigDecimal.ZERO;
        BigDecimal comparative1GT = BigDecimal.ZERO;
        BigDecimal comparative1SGT = BigDecimal.ZERO;
        BigDecimal comparative1TypeTotal = BigDecimal.ZERO;
        //comparative2
        BigDecimal comparative2LC  = BigDecimal.ZERO;
        BigDecimal comparative2GT = BigDecimal.ZERO;
        BigDecimal comparative2SGT = BigDecimal.ZERO;
        BigDecimal comparative2TypeTotal = BigDecimal.ZERO;
        for(Object[] obj: resultList){
            String group = (String) obj[0];
            String subGroup = (String) obj[1];
            String type = (String) obj[2];
            String acctCode = (String) obj[3];
            BigDecimal amount = (BigDecimal) obj[5];
            BigDecimal comparative1 = (BigDecimal) obj[6];
            BigDecimal comparative2 = (BigDecimal) obj[7];
            
            if(!latestType.equals(type)){
                if(!"".equals(latestType) && reportLevel != 3){
                    data.add(new Object[]{latestGroup, latestSubGroup, reportLevel > 2?"Total "+ latestType:""+ latestType, reportLevel > 2? "-" : "","", typeTotal, comparative1TypeTotal, comparative2TypeTotal});
                }
                typeTotal = BigDecimal.ZERO;
                comparative1TypeTotal = BigDecimal.ZERO;
                comparative2TypeTotal = BigDecimal.ZERO;
            }
            if(!latestSubGroup.equalsIgnoreCase(subGroup)){
                if(!"".equals(latestSubGroup)){
                    data.add(new Object[]{latestGroup,"Total "+ latestSubGroup,"-","","", subGroupTotal, comparative1SGT, comparative2SGT});
                }
                subGroupTotal = BigDecimal.ZERO;//reninitialize
                comparative1SGT = BigDecimal.ZERO;
                comparative2SGT = BigDecimal.ZERO;
            }
            if(!latestGroup.equalsIgnoreCase(group)){
                if(!"".equals(latestGroup)){
                    data.add(new Object[]{"Total "+latestGroup,"-","-","","", groupTotal, comparative1GT, comparative2GT});
                }
                data.add(emptyRow);
                data.add(new Object[]{group,""});
                latestGroup = group;
                groupTotal = BigDecimal.ZERO;//reninitialize
                comparative1GT =BigDecimal.ZERO;
                comparative2GT =BigDecimal.ZERO;
                
            }
            if(!latestSubGroup.equalsIgnoreCase(subGroup)){
                data.add(new Object[]{group,subGroup,""});
                latestSubGroup = subGroup;
            }
            if(!latestType.equals(type)){
                if(reportLevel == 4){
                    data.add(new Object[]{group, subGroup, type, ""});
                }
                latestType = type;
            }
            groupTotal = groupTotal.add(amount);
            subGroupTotal = subGroupTotal.add(amount);
            typeTotal = typeTotal.add(amount);
            comparative1GT = comparative1GT.add(comparative1);
            comparative1SGT = comparative1SGT.add(comparative1);
            comparative1TypeTotal = comparative1TypeTotal.add(comparative1);
            comparative2GT = comparative2GT.add(comparative2);
            comparative2SGT = comparative2SGT.add(comparative2);
            comparative2TypeTotal = comparative2TypeTotal.add(comparative2);
            if(("2-Liabilities".equalsIgnoreCase(group)|| "3-Capital".equalsIgnoreCase(group))){
                totalLiabilitiesAndCapital = totalLiabilitiesAndCapital.add(amount);
                comparative1LC = comparative1LC.add(comparative1);
                comparative2LC = comparative2LC.add(comparative2);
            }
            if(reportLevel != 2) data.add(obj);
            
        }
        if(reportLevel != 3){
            data.add(new Object[]{latestGroup, latestSubGroup, reportLevel > 2?"Total "+ latestType:""+ latestType, reportLevel > 2? "-" : "", typeTotal, comparative1TypeTotal, comparative2TypeTotal});
        }
        data.add(new Object[]{latestGroup,"Total "+ latestSubGroup,"-","","", subGroupTotal, comparative1SGT, comparative2SGT});
        data.add(new Object[]{"Total "+latestGroup,"-","-","","", groupTotal, comparative1GT, comparative2GT});
        data.add(new Object[]{"Total Liabilities & Capital","-", "","","", totalLiabilitiesAndCapital, comparative1LC, comparative2LC});
        data.add(emptyRow);
    }

    public List<Object[]> getData() {
        return data;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Date getReportDate() {
        return reportDate;
    }
}


package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.jsf.UtilFunctions;
import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;

import com.leansoftwarelabs.mypis.view.util.FormUtils;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import java.util.Random;

import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import javax.naming.CannotProceedException;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.YearMonth;
import org.joda.time.Years;

public class IncomeStatementForm {
    private GLEntryFacadeBean service;
    private List<Object[]> data;
    private Date startDate;
    private Date endDate;
    private Integer reportLevel = 3;
    private Integer periodComparative = 0;
    private String interval = "YEAR";


    @PostConstruct
    public void init() {
        Calendar cal = Calendar.getInstance();
        endDate = cal.getTime();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        startDate = cal.getTime();
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


    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }


    public void setPeriodComparative(Integer periodComparative) {
        this.periodComparative = periodComparative;
    }

    public Integer getPeriodComparative() {
        return periodComparative;
    }


    public void setReportLevel(Integer reportLevel) {
        this.reportLevel = reportLevel;
    }

    public Integer getReportLevel() {
        return reportLevel;
    }


    public List<Object[]> getData() {
        return data;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    
    public Date getDateOnInterval(Date date, int comparativeUnit){
        int myInterval = -1;
        int unit = 1;
        if ("YEAR".equals(getInterval())) {
            myInterval = 1;
        } else if ("MONTH".equals(getInterval())) {
            myInterval = 2;
        } else {
            myInterval = 6;
            unit = Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();
        }
        return UtilFunctions.dateAdd(date, myInterval, unit * comparativeUnit);
    }

    public String getComparative1Header() {
        Date myStartDate = getDateOnInterval(this.startDate, -1);
        Date myEndDate = getDateOnInterval(this.endDate, -1);
        StringBuilder b = new StringBuilder();
        b.append(UtilFunctions.formatDate(myStartDate, "yyyy-MM-dd"));
        b.append(" to ");
        b.append(UtilFunctions.formatDate(myEndDate, "yyyy-MM-dd"));
        return b.toString();
    }

    public String getComparative2Header() {
        Date myStartDate = getDateOnInterval(this.startDate, -2);
        Date myEndDate = getDateOnInterval(this.endDate, -2);
        StringBuilder b = new StringBuilder();
        b.append(UtilFunctions.formatDate(myStartDate, "yyyy-MM-dd"));
        b.append(" to ");
        b.append(UtilFunctions.formatDate(myEndDate, "yyyy-MM-dd"));
        return b.toString();
    }


    public void runReport(ActionEvent actionEvent) {
        int unit = 0;
        if ("MONTH".equals(interval) || "YEAR".equals(interval)) {
            unit = 1;
        } else {
            unit = Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();
        }
        List<Object[]> resultList = null;
        if (reportLevel > 2) {
            resultList = getService().getIncomeStatementData(startDate, endDate, periodComparative, unit, interval);
        } else {
            resultList =
                getService().getIncomeStatementDataDetailedByType(startDate, endDate, periodComparative, unit,
                                                                  interval);
        }
        data = new ArrayList<Object[]>();
        String latestSubGroup = "";
        String latestType = "";
        Object[] emptyRow = new Object[] { "", "", "", "", null };
        BigDecimal subGroupTotal = BigDecimal.ZERO;
        BigDecimal typeTotal = BigDecimal.ZERO;
        //comparative1
        BigDecimal comparative1SGT = BigDecimal.ZERO;
        BigDecimal comparative1TypeTotal = BigDecimal.ZERO;
        //comparative2
        BigDecimal comparative2SGT = BigDecimal.ZERO;
        BigDecimal comparative2TypeTotal = BigDecimal.ZERO;
        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal revenueC1 = BigDecimal.ZERO;
        BigDecimal revenueC2 = BigDecimal.ZERO;
        BigDecimal cost = BigDecimal.ZERO;
        BigDecimal costC1 = BigDecimal.ZERO;
        BigDecimal costC2 = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;
        BigDecimal expensesC1 = BigDecimal.ZERO;
        BigDecimal expensesC2 = BigDecimal.ZERO;
        for (Object[] obj : resultList) {
            String subGroup = (String) obj[0];
            String type = (String) obj[1];
            String acctCode = (String) obj[2];
            BigDecimal amount = (BigDecimal) obj[4];
            BigDecimal comparative1 = (BigDecimal) obj[5];
            BigDecimal comparative2 = (BigDecimal) obj[6];
            if (!latestSubGroup.equalsIgnoreCase(subGroup)) {
                if (!"".equals(latestSubGroup)) {
                    data.add(new Object[] {
                             "Total " + latestSubGroup, "", "", -1, subGroupTotal, comparative1SGT, comparative2SGT });
                    if ("Revenue".equalsIgnoreCase(latestSubGroup)) {
                        revenue = subGroupTotal;
                        revenueC1 = comparative1SGT;
                        revenueC2 = comparative2SGT;
                    }
                    if ("Cost".equalsIgnoreCase(latestSubGroup)) {
                        cost = subGroupTotal;
                        costC1 = comparative1SGT;
                        costC2 = comparative2SGT;
                        data.add(new Object[] {
                                 "Gross Profit", "", "", -1, revenue.subtract(cost), revenueC1.subtract(costC1),
                                 revenueC2.subtract(costC2),
                        });
                    }
                    //means expenses is not the last type
                    if ("Expenses".equalsIgnoreCase(latestSubGroup)) {
                        expenses = subGroupTotal;
                        expensesC1 = comparative1SGT;
                        expensesC2 = comparative2SGT;
                        data.add(new Object[] {
                                 "Operating Profit", "", "", -1, revenue.subtract(cost).subtract(expenses),
                                 revenueC1.subtract(costC1).subtract(expensesC1),
                                 revenueC2.subtract(costC2).subtract(expensesC2)
                        });
                    }
                    data.add(emptyRow);
                }
                subGroupTotal = BigDecimal.ZERO; //reninitialize
                comparative1SGT = BigDecimal.ZERO;
                comparative2SGT = BigDecimal.ZERO;
                data.add(new Object[] { subGroup, "", "", "", null });
                latestSubGroup = subGroup;
            }
            obj[0] = "";
            if (reportLevel > 2) {
                obj[1] = "";
            }
            data.add(obj);
            subGroupTotal = subGroupTotal.add(amount);
            typeTotal = typeTotal.add(amount);
            comparative1SGT = comparative1SGT.add(comparative1);
            comparative1TypeTotal = comparative1TypeTotal.add(comparative1);
            comparative2SGT = comparative2SGT.add(comparative2);
            comparative1TypeTotal = comparative2TypeTotal.add(comparative2);
        }
        data.add(new Object[] {
                 "Total " + latestSubGroup, "", "", -1, subGroupTotal, comparative1SGT, comparative2SGT });
        data.add(emptyRow);
        data.add(new Object[] {
                 "Net Income", "", "", -1, revenue.subtract(cost).subtract(expenses).subtract(subGroupTotal),
                 revenueC1.subtract(costC1).subtract(expensesC1).subtract(comparative1SGT),
                 revenueC2.subtract(costC2).subtract(expensesC2).subtract(comparative2SGT)
        });
        data.add(emptyRow);
    }

    public void viewAccountSummary(ActionEvent actionEvent) {
        UIComponent comp = actionEvent.getComponent();
        Date startDate = (Date) comp.getAttributes().get("startDate");
        Date endDate = (Date) comp.getAttributes().get("endDate");
        Integer accountId = (Integer) comp.getAttributes().get("accountId");
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("title", "Account Summary");
        payload.put("taskFlowId", "/WEB-INF/taskflows/acctg/account-summary.xml#account-summary");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("accountId", accountId);
        parameters.put("endDate", endDate);
        parameters.put("startDate", startDate);
        parameters.put("KEY", UUID.randomUUID());//always new tab
        payload.put("parameters", parameters);
        payload.put("newTab", true);
        FormUtils.raiseEvent("launchActivity", payload);
    }
    
}

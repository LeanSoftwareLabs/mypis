package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.ListOfValuesModelImpl;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.ext.ServiceProvider;
import com.leansoftwarelabs.mypis.domain.GLAccount;

import com.leansoftwarelabs.mypis.domain.TrackingCategory;
import com.leansoftwarelabs.mypis.service.GLAccountFacadeBean;
import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;
import com.leansoftwarelabs.mypis.service.TrackingCategoryFacadeBean;
import com.leansoftwarelabs.mypis.view.util.AttributeDefMapProviderUtil;
import com.leansoftwarelabs.mypis.view.util.FormUtils;
import com.leansoftwarelabs.trinidad.model.FilterableCollectionModel;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;

import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.ListOfValuesModel;

public class AccountSummaryForm {
    private List<Object[]> data;
    private List<Object[]> lineGraphData;
    private Date startDate;
    private Date endDate;
    private GLAccount account;
    private ListOfValuesModel glAccountLOVModel;
    private List<String[]> extraHeadings;
    List<Object[]> trackingInfoList = new ArrayList<Object[]>();

    @PostConstruct
    public void init() {
        ServiceProvider<TrackingCategoryFacadeBean> trackingCategoryFacadeProvider = FormUtils.getService("trackingCategoryFacade");
        List<TrackingCategory> trackingCategoryList = trackingCategoryFacadeProvider.getService().findAll();
        for(TrackingCategory category: trackingCategoryList){
            trackingInfoList.add(new Object[] { category, null });
        }
        startDate = (Date) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("startDate");
        endDate = (Date) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("endDate");
        Integer accountId = (Integer) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("accountId");
        //no input parameter
        if (endDate == null) {
            Calendar cal = Calendar.getInstance();
            endDate = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
        }
        if (accountId != null) {
            ServiceProvider<GLAccountFacadeBean> glAccountFacadeProvider = FormUtils.getService("glAccountFacade");
            account = glAccountFacadeProvider.getService().find(accountId);
            runReport(null);
        }

    }


    public List<Object[]> getData() {
        return data;
    }


    public List<Object[]> getLineGraphData() {
        return lineGraphData;
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


    public void setAccount(GLAccount account) {
        this.account = account;
    }

    public GLAccount getAccount() {
        return account;
    }

    public ListOfValuesModel getGlAccountLOVModel() {
        if (glAccountLOVModel == null) {
            Map<String, AttributeDef> attributes = AttributeDefMapProviderUtil.getGLAccountAttributeDefMap();
            QueryModelImpl queryModel = new QueryModelImpl("GLAccountQuery", attributes, null, null);
            final ServiceProvider<GLAccountFacadeBean> glAccountFacade = FormUtils.getService("glAccountFacade");
            FilterableCollectionModel collectionModel = new JpqlLazyDataModel("GLAccount", 20) {
                protected List queryByRange(String jpqlStmt, int first, int pageSize) {
                    return glAccountFacade.getService().queryByRange(jpqlStmt, null, first, pageSize);
                }
            };
            glAccountLOVModel = new ListOfValuesModelImpl(new String[] { "code", "name" }, queryModel, collectionModel);
        }
        return glAccountLOVModel;
    }

    public void runReport(ActionEvent actionEvent) {
        ServiceProvider<GLEntryFacadeBean> serviceProvider = FormUtils.getService("glEntryFacade");
        extraHeadings = serviceProvider.getService().buildCriteriaList(trackingInfoList);
        data = new ArrayList<Object[]>();
        lineGraphData = new ArrayList<Object[]>();
        List<Object[]> result =
            serviceProvider.getService().getAccountSummaryData(account.getAccountId(), account.getType().getNormalBalance() , startDate, endDate, trackingInfoList);
//        List<Object[]> result =
//            serviceProvider.getService().getAccountSummaryData(account.getAccountId(), startDate, endDate);
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal[] total = new BigDecimal[extraHeadings.size()];
        for(int i = 0; i< total.length; i++){
            total[i] = BigDecimal.ZERO;
        }
        for (Object[] obj : result) {
            BigDecimal debit = (obj[3] == null ? BigDecimal.ZERO : (BigDecimal) obj[3]);
            BigDecimal credit = (obj[4] == null ? BigDecimal.ZERO : (BigDecimal) obj[4]);
            BigDecimal amount = (obj[5] == null ? BigDecimal.ZERO : (BigDecimal) obj[5]);
            totalDebit = totalDebit.add(debit);
            totalCredit = totalCredit.add(credit);
            totalAmount = totalAmount.add(amount);
            data.add(obj);
            lineGraphData.add(new Object[]{obj[2],"Actual",amount});
            for(int i = 0; i< total.length; i++){
                BigDecimal amt = (BigDecimal) (obj[i + 6] == null ? BigDecimal.ZERO : obj[i + 6]);
                total[i] = total[i].add(amt);
                lineGraphData.add(new Object[]{obj[2],extraHeadings.get(i)[1],amt});
            }
        }
        Object[] totals = new Object[6 + total.length];
        totals[0] = -1;
        totals[1] = -1;
        totals[2] = "Total";
        totals[3] = totalDebit;
        totals[4] = totalCredit;
        totals[5] = totalAmount;
        for(int i = 0; i< total.length; i++){
            totals[i+6] = total[i];
        }
        data.add(totals);
        if (totalDebit.compareTo(totalCredit) > 0) {
            data.add(new Object[] { -1, -1, "Net Amount", totalDebit.subtract(totalCredit) });
        } else {
            data.add(new Object[] { -1, -1, "Net Amount", "", totalCredit.subtract(totalDebit) });
        }
        data.add(new Object[] { });
    }

    public void viewAccountDetail(ActionEvent actionEvent) {
        UIComponent comp = actionEvent.getComponent();
        Integer year = (Integer) comp.getAttributes().get("year");
        Integer month = (Integer) comp.getAttributes().get("month");
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        Date startDate = cal.getTime();
        cal.set(year, month, 0);
        Date endDate = cal.getTime();
        if (startDate.before(this.startDate)) {
            startDate = this.startDate;
        }
        if (endDate.after(this.endDate)) {
            endDate = this.endDate;
        }
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("title", "Account Detail");
        payload.put("taskFlowId", "/WEB-INF/taskflows/acctg/account-detail.xml#account-detail");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("accountId", account.getAccountId());
        parameters.put("endDate", endDate);
        parameters.put("startDate", startDate);
        parameters.put("KEY", UUID.randomUUID()); //always new tab
        payload.put("parameters", parameters);
        payload.put("newTab", true);
        FormUtils.raiseEvent("launchActivity", payload);
    }
    
    public List<String[]> getExtraHeadings(){
        return extraHeadings;
    }


    public List<Object[]> getTrackingInfoList() {
        return trackingInfoList;
    }

}

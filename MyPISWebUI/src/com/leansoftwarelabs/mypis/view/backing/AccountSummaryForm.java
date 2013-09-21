package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.ListOfValuesModelImpl;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.ext.ServiceProvider;
import com.leansoftwarelabs.mypis.domain.GLAccount;

import com.leansoftwarelabs.mypis.service.GLAccountFacadeBean;
import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;
import com.leansoftwarelabs.mypis.view.util.AttributeDefMapProviderUtil;
import com.leansoftwarelabs.mypis.view.util.FormUtils;
import com.leansoftwarelabs.trinidad.model.FilterableCollectionModel;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

    @PostConstruct
    public void init() {
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
        data = new ArrayList<Object[]>();
        lineGraphData = new ArrayList<Object[]>();
        List<Object[]> result =
            serviceProvider.getService().getAccountSummaryData(account.getAccountId(), startDate, endDate);
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        for (Object[] obj : result) {
            BigDecimal debit = (obj[3] == null ? BigDecimal.ZERO : (BigDecimal) obj[3]);
            BigDecimal credit = (obj[4] == null ? BigDecimal.ZERO : (BigDecimal) obj[4]);
            totalDebit = totalDebit.add(debit);
            totalCredit = totalCredit.add(credit);
            data.add(obj);
            lineGraphData.add(new Object[]{obj[2],"Actual",debit.subtract(credit)});
        }
        data.add(new Object[] { -1, -1, "Total", totalDebit, totalCredit });
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
}

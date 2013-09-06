package com.leansoftwarelabs.mypis.view.backing;


import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.domain.GLAccount;
import com.leansoftwarelabs.mypis.service.BaptismalRegisterFacadeBean;
import com.leansoftwarelabs.mypis.service.GLAccountFacadeBean;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.trinidad.model.LazyDataModel;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.event.RegionNavigationEvent;
import oracle.adf.view.rich.model.QueryModel;

public class GLAccountListForm {
    private GLAccountFacadeBean service;
    private QueryModel queryModel;
    private LazyDataModel lazyDataModel;
    private RichTable glAccountTable;
    private RichPopup glAccountDetailPopup;
    private RichRegion region;

    @PostConstruct
    public void init() {
        Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
        attributes.put("code", new AttributeDef("code", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("name", new AttributeDef("name", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("description", new AttributeDef("description", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("dashboard", new AttributeDef("dashboard", Boolean.class, null, AttributeDef.CHECK_BOX));
        attributes.put("expenseClaims", new AttributeDef("expenseClaims", Boolean.class, null, AttributeDef.CHECK_BOX));
        attributes.put("payments", new AttributeDef("payments", Boolean.class, null, AttributeDef.CHECK_BOX));

        queryModel = new QueryModelImpl("GLAccountQuery", attributes, null, null);
    }

    public LazyDataModel getLazyDataModel() {
        if (lazyDataModel == null) {
            lazyDataModel = new JpqlLazyDataModel("GLAccount", 20) {
                protected List queryByRange(String jpqlStmt, int first, int pageSize) {
                    return (List) getService().queryByRange(jpqlStmt, null, first, pageSize);
                }
            };
        }
        return lazyDataModel;
    }

    public QueryModel getQueryModel() {
        return queryModel;
    }

    public GLAccountFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service = (GLAccountFacadeBean) context.lookup("java:comp/env/ejb/local/GLAccountFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public void tableFilter(QueryEvent event) {
        this.lazyDataModel.setFilterCriteria(event.getDescriptor().getConjunctionCriterion());
    }

    public void viewDetails(ActionEvent ev) {
        GLAccount glAccount = (GLAccount) getGlAccountTable().getSelectedRowData();
        Integer accountId = glAccount.getAccountId();
        String title = "GL Account: " + glAccount.getCode();
        launchActivity(accountId, title);
    }


    private void launchActivity(Integer accountId, String title) {
        ADFUtils.getPageFlowScope().put("accountId", accountId);
        ADFUtils.getPageFlowScope().put("dialogTitle", title);
        RichRegion region = getRegion();
        if (region != null) {
            getRegion().refresh(FacesContext.getCurrentInstance());
        }
        ADFUtils.showDialog(getGlAccountDetailPopup());
    }

    //    private void launchActivity(Integer accountId, String title) {
    //        Map<String, Object> payload = new HashMap<String, Object>();
    //        payload.put("title", title);
    //        payload.put("taskFlowId", "/WEB-INF/taskflows/acctg/gl-account-entry.xml#gl-account-entry");
    //        Map<String, Object> parameters = new HashMap<String, Object>();
    //        parameters.put("accountId", accountId);
    //        parameters.put("KEY", accountId);
    //        payload.put("parameters", parameters);
    //        payload.put("newTab", true);
    //        raiseEvent("launchActivity", payload);
    //    }


    public void create(ActionEvent ev) {
        launchActivity(-1, "Create New GL Account");
    }


    public void setGlAccountTable(RichTable glAccountTable) {
        this.glAccountTable = glAccountTable;
    }

    public RichTable getGlAccountTable() {
        return glAccountTable;
    }

    public void setGlAccountDetailPopup(RichPopup glAccountDetailPopup) {
        this.glAccountDetailPopup = glAccountDetailPopup;
    }

    public RichPopup getGlAccountDetailPopup() {
        return glAccountDetailPopup;
    }

    public void regionNavigationListener(RegionNavigationEvent regionNavigationEvent) {
        String newViewId = regionNavigationEvent.getNewViewId();
        if (newViewId == null) {
            //there is no turning back
            //trans committed or rolledback already
            ADFUtils.closeDialog(getGlAccountDetailPopup());
        }
    }
    
    public Map getAccountTypeItems(){
        return GLAccount.AccountType.VALUES;
    }

    public void setRegion(RichRegion region) {
        this.region = region;
    }

    public RichRegion getRegion() {
        return region;
    }
}

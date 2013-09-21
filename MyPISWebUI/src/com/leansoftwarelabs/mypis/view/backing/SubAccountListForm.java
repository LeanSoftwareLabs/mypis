package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.ListOfValuesModelImpl;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.mypis.domain.GLEntry;
import com.leansoftwarelabs.mypis.domain.SubAccount;
import com.leansoftwarelabs.mypis.service.SubAccountFacadeBean;
import com.leansoftwarelabs.mypis.view.util.FormUtils;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.trinidad.model.LazyDataModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.ListOfValuesModel;
import oracle.adf.view.rich.model.QueryModel;

import org.apache.shiro.authc.Account;

public class SubAccountListForm {
    private SubAccountFacadeBean service;
    private QueryModel queryModel;
    private LazyDataModel lazyDataModel;
    private RichTable richTable;
    
    private ListOfValuesModel subAccountLOVModel;


    public ListOfValuesModel getSubAccountLOVModel() {
        if(subAccountLOVModel == null){
            subAccountLOVModel = new ListOfValuesModelImpl(new String[]{"code", "name"}, (QueryModelImpl)getQueryModel(), getLazyDataModel());
        }
        return subAccountLOVModel;
    }


    public SubAccountFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service = (SubAccountFacadeBean) context.lookup("java:comp/env/ejb/local/SubAccountFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public QueryModel getQueryModel() {
        if (queryModel == null) {
            Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
            attributes.put("code", new AttributeDef("code", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("name", new AttributeDef("name", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("description", new AttributeDef("description", String.class, null, AttributeDef.INPUT_TEXT));
                        queryModel = new QueryModelImpl("GLEntryQuery", attributes, null, null);
        }
        return queryModel;
    }
    
    public void tableFilter(QueryEvent event) {
        this.lazyDataModel.setFilterCriteria(event.getDescriptor().getConjunctionCriterion());
    }

    public LazyDataModel getLazyDataModel() {
        if (lazyDataModel == null) {
            lazyDataModel = new JpqlLazyDataModel("SubAccount", 20) {
                protected List queryByRange(String jpqlStmt, int first, int pageSize) {
                    return getService().queryByRange(jpqlStmt, null, first, pageSize);
                }
            };
        }
        return lazyDataModel;
    }
    
    

    public void setRichTable(RichTable richTable) {
        this.richTable = richTable;
    }

    public RichTable getRichTable() {
        return richTable;
    }
    
    public void viewDetails(ActionEvent ev) {
        SubAccount subAccount = (SubAccount) getRichTable().getSelectedRowData();
        Integer key = subAccount.getId();
        String title = subAccount.getCode() + ":" +subAccount.getName();
        launchActivity(key, title, false);
    }

    private void launchActivity(Integer key, String title, boolean editMode) {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("title", title);
        payload.put("taskFlowId", "/WEB-INF/taskflows/acctg/sub-account-entry.xml#sub-account-entry");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("subAccountId", key);
        parameters.put("KEY", key);
        parameters.put("editMode", editMode);
        payload.put("parameters", parameters);
        payload.put("newTab", true);
        FormUtils.raiseEvent("launchActivity", payload);
    }

    public void create(ActionEvent ev) {
        launchActivity(-1, "New SubAccount", true);
    }

}

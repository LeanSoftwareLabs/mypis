package com.leansoftwarelabs.mypis.view.backing;


import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.service.BaptismalRegisterFacadeBean;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.trinidad.model.LazyDataModel;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.QueryModel;

public class BaptismalRegisterListForm {
    private BaptismalRegisterFacadeBean service;
    private QueryModel queryModel;
    private LazyDataModel lazyDataModel;
    private RichTable baptismalRegisterTable;

    @PostConstruct
    public void init() {
        Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
        attributes.put("volume", new AttributeDef("volume", Integer.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("page", new AttributeDef("page", Integer.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("line", new AttributeDef("line", Integer.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("lastName", new AttributeDef("lastName", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("firstName", new AttributeDef("firstName", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("birthDate", new AttributeDef("birthDate", Date.class, null, AttributeDef.INPUT_DATE));

        queryModel = new QueryModelImpl("baptismalRegisterQuery", attributes, null, null);
    }

    public LazyDataModel getLazyDataModel() {
        if (lazyDataModel == null) {
            lazyDataModel = new JpqlLazyDataModel("BaptismalRegister", 30) {
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

    public BaptismalRegisterFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (BaptismalRegisterFacadeBean) context.lookup("java:comp/env/ejb/local/BaptismalRegisterFacade");
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
        BaptismalRegister baptismalRegister = (BaptismalRegister) getBaptismalRegisterTable().getSelectedRowData();
        Integer registerId = baptismalRegister.getRegisterId();
        String title =
            "Baptismal: " + baptismalRegister.getRegisterId() + ":" + baptismalRegister.getLastName() + ", " +
            baptismalRegister.getFirstName();
        launchActivity(registerId, title);
    }

    private void launchActivity(Integer registerId, String title) {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("title", title);
        payload.put("taskFlowId", "/WEB-INF/taskflows/baptismal-register-entry.xml#baptismal-register-entry");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("registerId", registerId);
        parameters.put("KEY", registerId);
        payload.put("parameters", parameters);
        payload.put("newTab", true);
        raiseEvent("launchActivity", payload);
    }

    public void create(ActionEvent ev) {
        launchActivity(-1, "New Baptismal Entry");
    }

    private static void raiseEvent(String eventName, Map<String, Object> payload) {
        EventHandler eventHandler = (EventHandler) ADFUtils.getPageFlowScope().get("eventHandler");
        eventHandler.handleEvent(eventName, payload);
    }

    public void setBaptismalRegisterTable(RichTable baptismalRegisterTable) {
        this.baptismalRegisterTable = baptismalRegisterTable;
    }

    public RichTable getBaptismalRegisterTable() {
        return baptismalRegisterTable;
    }
}

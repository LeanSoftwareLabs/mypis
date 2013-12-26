package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.mypis.domain.GLEntry;
import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;
import com.leansoftwarelabs.mypis.view.util.FormUtils;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.trinidad.model.LazyDataModel;

import java.math.BigDecimal;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.QueryModel;

public class GeneralLedgerForm {
    private GLEntryFacadeBean service;
    private QueryModel queryModel;
    private LazyDataModel lazyDataModel;
    private RichTable richTable;

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

    public QueryModel getQueryModel() {
        if (queryModel == null) {
            Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
            attributes.put("myReference", new AttributeDef("myReference", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("yourReference",
                           new AttributeDef("yourReference", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("description", new AttributeDef("description", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("transDate", new AttributeDef("transDate", Date.class, null, AttributeDef.INPUT_DATE));
            attributes.put("reportingDate",
                           new AttributeDef("reportingDate", Date.class, null, AttributeDef.INPUT_DATE));
            attributes.put("totalDebit",
                           new AttributeDef("totalDebit", BigDecimal.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("totalCredit",
                           new AttributeDef("totalCredit", BigDecimal.class, null, AttributeDef.INPUT_TEXT));

            queryModel = new QueryModelImpl("GLEntryQuery", attributes, null, null);
        }
        return queryModel;
    }

    public void tableFilter(QueryEvent event) {
        this.lazyDataModel.setFilterCriteria(event.getDescriptor().getConjunctionCriterion());
    }

    public LazyDataModel getLazyDataModel() {
        if (lazyDataModel == null) {
            lazyDataModel = new JpqlLazyDataModel("GLEntry", 20) {
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
        GLEntry entry = (GLEntry) getRichTable().getSelectedRowData();
        Integer key = entry.getId();
        String title = entry.getMyReference();
        if ("PAY".equals(entry.getTransType())) {
            launchActivity("/WEB-INF/taskflows/acctg/cash-voucher.xml#cash-voucher", key, title, false);
        } else {
            launchActivity("/WEB-INF/taskflows/acctg/general-ledger-entry.xml#general-ledger-entry", key, title, false);
        }
    }

    private void launchActivity(String taskFlowId, Integer key, String title, boolean editMode) {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("title", title);
        payload.put("taskFlowId", taskFlowId);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("glEntryId", key);
        parameters.put("KEY", key);
        parameters.put("editMode", editMode);
        payload.put("parameters", parameters);
        payload.put("newTab", true);
        FormUtils.raiseEvent("launchActivity", payload);
    }

    public void create(ActionEvent ev) {
        launchActivity("/WEB-INF/taskflows/acctg/general-ledger-entry.xml#general-ledger-entry", -1, "New GL Entry",
                       true);
    }


}

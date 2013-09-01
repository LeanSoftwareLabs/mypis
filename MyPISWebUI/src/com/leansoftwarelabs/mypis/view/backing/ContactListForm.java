package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.mypis.service.ContactFacadeBean;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.trinidad.model.LazyDataModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.QueryModel;

public class ContactListForm {
    private ContactFacadeBean service;
    private QueryModel queryModel;
    private LazyDataModel lazyDataModel;
    private RichTable contactTable;
    public ContactListForm() {
        super();
    }

    public ContactFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (ContactFacadeBean) context.lookup("java:comp/env/ejb/local/ContactFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }
    @PostConstruct
    public void init() {
        Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
        attributes.put("name", new AttributeDef("name", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("email", new AttributeDef("email", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("postalCity", new AttributeDef("postalCity", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("customGroups", new AttributeDef("customGroups", String.class, null, AttributeDef.INPUT_DATE));
        attributes.put("employee", new AttributeDef("employee", Boolean.class, null, AttributeDef.CHECK_BOX));
        attributes.put("customer", new AttributeDef("customer", Boolean.class, null, AttributeDef.CHECK_BOX));
        attributes.put("supplier", new AttributeDef("supplier", Boolean.class, null, AttributeDef.CHECK_BOX));

        queryModel = new QueryModelImpl("ContactQuery", attributes, null, null);
    }


    public QueryModel getQueryModel() {
        return queryModel;
    }
    
    public void tableFilter(QueryEvent event) {
        this.lazyDataModel.setFilterCriteria(event.getDescriptor().getConjunctionCriterion());
    }

    public LazyDataModel getLazyDataModel() {
        if (lazyDataModel == null) {
            lazyDataModel = new JpqlLazyDataModel("Contact", 30) {
                protected List queryByRange(String jpqlStmt, int first, int pageSize) {
                    return (List) getService().queryByRange(jpqlStmt, null, first, pageSize);
                }
            };
        }
        return lazyDataModel;
    }

    public void setContactTable(RichTable contactTable) {
        this.contactTable = contactTable;
    }

    public RichTable getContactTable() {
        return contactTable;
    }
}

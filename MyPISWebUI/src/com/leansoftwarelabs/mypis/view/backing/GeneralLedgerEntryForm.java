package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.adf.query.AttributeDef;
import com.leansoftwarelabs.adf.query.ListOfValuesModelImpl;
import com.leansoftwarelabs.adf.query.QueryModelImpl;
import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.mypis.domain.GLEntry;

import com.leansoftwarelabs.mypis.domain.GLEntryLine;
import com.leansoftwarelabs.mypis.service.GLAccountFacadeBean;
import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;
import com.leansoftwarelabs.trinidad.model.FilterableCollectionModel;
import com.leansoftwarelabs.trinidad.model.JpqlLazyDataModel;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.model.ListOfValuesModel;

import oracle.net.aso.g;

public class GeneralLedgerEntryForm {
    private GLEntry entry;
    private GLEntryFacadeBean service;
    private RichTable entryLineTable;
    private ListOfValuesModel glAccountLOVModel;
    private GLAccountFacadeBean glAccountFacade;

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


    public GLAccountFacadeBean getGLAccountFacade() {
        if(glAccountFacade == null){
            try {
                final Context context = new InitialContext();
                glAccountFacade = (GLAccountFacadeBean) context.lookup("java:comp/env/ejb/local/GLAccountFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return glAccountFacade;
    }

    @PostConstruct
    public void init(){
        Integer glEntryId = (Integer) ADFUtils.getPageFlowScope().get("glEntryId");
        if(glEntryId == -1){
            entry = new GLEntry();
            entry.setTransType("JV");
            entry.setTransDate(Calendar.getInstance().getTime());
            entry.setReportingDate(entry.getTransDate());
            GLEntryLine entryLine = new GLEntryLine();
            entry.addGLEntryLine(entryLine);
        }else{
            entry = getService().find(glEntryId);
        }
    }
    
    public void edit() {
        FormUtils.editing(true);
    }

    public String cancel() {
        FormUtils.editing(false);
        if (entry.getId() == null) {
            return "done";
        } else {
            init();
        }
        return null;
    }

   

    public void save() {
        entry = getService().mergeEntity(entry);
        FormUtils.editing(false);
    }

    public GLEntry getEntry() {
        return entry;
    }

    public GeneralLedgerEntryForm() {
        super();
    }

    public void addLine(ActionEvent actionEvent) {
        GLEntryLine entryLine = new GLEntryLine();
        entry.addGLEntryLine(entryLine);
    }

    public void removeLine(ActionEvent actionEvent) {
        GLEntryLine entryLine = (GLEntryLine) getEntryLineTable().getSelectedRowData();
        entry.removeGLEntryLine(entryLine);
    }


    public void setEntryLineTable(RichTable entryLineTable) {
        this.entryLineTable = entryLineTable;
    }

    public RichTable getEntryLineTable() {
        return entryLineTable;
    }


    public ListOfValuesModel getGlAccountLOVModel() {
        if(glAccountLOVModel == null){
            Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
            attributes.put("code", new AttributeDef("code", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("name", new AttributeDef("name", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("description", new AttributeDef("description", String.class, null, AttributeDef.INPUT_TEXT));
            attributes.put("dashboard", new AttributeDef("dashboard", Boolean.class, null, AttributeDef.CHECK_BOX));
            attributes.put("expenseClaims", new AttributeDef("expenseClaims", Boolean.class, null, AttributeDef.CHECK_BOX));
            attributes.put("payments", new AttributeDef("payments", Boolean.class, null, AttributeDef.CHECK_BOX));
            QueryModelImpl queryModel = new QueryModelImpl("GLAccountQuery", attributes, null, null);
            FilterableCollectionModel collectionModel = new JpqlLazyDataModel("GLAccount", 20) {
                protected List queryByRange(String jpqlStmt, int first, int pageSize) {
                    return getGLAccountFacade().queryByRange(jpqlStmt, null, first, pageSize);
                }
            };
            glAccountLOVModel = new ListOfValuesModelImpl("code", queryModel, collectionModel);
        }
        return glAccountLOVModel;
    }


}

package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.domain.ConfirmationRegister;
import com.leansoftwarelabs.mypis.domain.ConfirmationRegister;
import com.leansoftwarelabs.mypis.service.ConfirmationRegisterFacadeLocal;

import com.leansoftwarelabs.mypis.service.ConfirmationRegisterFacadeLocal;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

public class ConfirmationRegisterEntryForm {
    private ConfirmationRegister confirmationRegister;
    private ConfirmationRegisterFacadeLocal service;

    @PostConstruct
    public void init() {
        Integer registerId = (Integer) ADFUtils.getPageFlowScope().get("registerId");
        if (registerId == -1) {
            confirmationRegister = new ConfirmationRegister();
        } else {
            confirmationRegister = getService().findConfirmationRegisterById(registerId);
            
        }
    }

    public void setConfirmationRegister(ConfirmationRegister baptismalRegister) {
        this.confirmationRegister = confirmationRegister;
    }

    public ConfirmationRegister getCOnfirmationRegister() {
        return confirmationRegister;
    }

    public ConfirmationRegisterFacadeLocal getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (ConfirmationRegisterFacadeLocal) context.lookup("java:comp/env/ejb/local/ConfirmationRegisterFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public void edit() {
        editing(true);
    }

    public String cancel() {
        editing(false);
        if(confirmationRegister.getConfirmationRegisterId() == 0){
            return "done";
        }
        return null;
    }

    private void editing(boolean editMode) {
        ADFUtils.getPageFlowScope().put("editMode", editMode);
        EventHandler eventHandler = (EventHandler) ADFUtils.getPageFlowScope().get("eventHandler");
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("isDirty", editMode);
        eventHandler.handleEvent("dirtyEvent", payload);
    }

    public void save() {
        this.confirmationRegister = getService().mergeEntity(this.confirmationRegister);
        editing(false);
    }
}

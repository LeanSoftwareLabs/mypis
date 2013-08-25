package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.service.BaptismalRegisterFacadeLocal;

import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

public class BaptismalRegisterEntryForm {
    private BaptismalRegister baptismalRegister;
    private BaptismalRegisterFacadeLocal service;

    @PostConstruct
    public void init() {
        Integer registerId = (Integer) ADFUtils.getPageFlowScope().get("registerId");
        if (registerId == -1) {
            baptismalRegister = new BaptismalRegister();
        } else {
            baptismalRegister = getService().findBaptismalRegisterById(registerId);
        }
    }

    public void setBaptismalRegister(BaptismalRegister baptismalRegister) {
        this.baptismalRegister = baptismalRegister;
    }

    public BaptismalRegister getBaptismalRegister() {
        return baptismalRegister;
    }

    public BaptismalRegisterFacadeLocal getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (BaptismalRegisterFacadeLocal) context.lookup("java:comp/env/ejb/local/BaptismalRegisterFacade");
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
        if(baptismalRegister.getRegisterId() == null){
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
        this.baptismalRegister = getService().mergeEntity(this.baptismalRegister);
        editing(false);
    }
}

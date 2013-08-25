package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.mypis.domain.BaptismalRegister;
import com.leansoftwarelabs.mypis.service.BaptismalRegisterFacadeLocal;

import com.leansoftwarelabs.view.utils.ADFUtils;

import javax.annotation.PostConstruct;

import javax.naming.Context;
import javax.naming.InitialContext;

public class BaptismalRegisterEntryForm {
    private BaptismalRegister baptismalRegister;
    private BaptismalRegisterFacadeLocal service;
    
    @PostConstruct
    public void init(){
        Integer registerId = (Integer) ADFUtils.getPageFlowScope().get("registerId");
        baptismalRegister = getService().findBaptismalRegisterById(registerId);
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
}

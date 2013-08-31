package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.realm.domain.Tenant;
import com.leansoftwarelabs.realm.service.TenantFacadeBean;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.context.AdfFacesContext;

public class TenantInformationForm {
    private Tenant tenant;
    private TenantFacadeBean service;

    public TenantInformationForm() {
        super();
    }

    @PostConstruct
    public void init() {
        Integer tenantId = (Integer) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("tenantId");
        this.tenant = getService().find(tenantId);
    }


    public Tenant getTenant() {
        return tenant;
    }


    public TenantFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service = (TenantFacadeBean) context.lookup("java:comp/env/ejb/local/TenantFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public void saveTenant(ActionEvent actionEvent) {
        getService().mergeEntity(tenant);
    }
}

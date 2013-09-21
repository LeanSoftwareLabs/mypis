package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.mypis.domain.SubAccount;
import com.leansoftwarelabs.mypis.service.ApplicationConstraintViolationException;
import com.leansoftwarelabs.mypis.service.ServiceException;
import com.leansoftwarelabs.mypis.service.SubAccountFacadeBean;

import com.leansoftwarelabs.mypis.view.util.FormUtils;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.context.AdfFacesContext;

public class SubAccountEntryForm {
    private SubAccountFacadeBean service;
    private SubAccount subAccount;


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

    @PostConstruct
    public void init() {
        Integer subAccountId = (Integer) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("subAccountId");
        if (subAccountId == -1) {
            subAccount = new SubAccount();
        } else {
            subAccount = getService().find(subAccountId);
        }
    }

    public SubAccount getSubAccount() {
        return subAccount;
    }

    public String edit() {
        FormUtils.editing(true);
        return null;
    }

    public String cancel() {
        FormUtils.editing(false);
        if (subAccount.getId() == null) {
            return "done";
        } else {
            init();
        }
        return null;
    }


    public String save() {
        try {
            this.subAccount = getService().mergeEntity(this.subAccount);
            FormUtils.editing(false);
        } catch (ApplicationConstraintViolationException ce) {
            FormUtils.addFacesErrorMessage(ce.getViolations());
        } catch (ServiceException se) {
            FormUtils.addFacesErrorMessage(se);
        }
        return null;
    }

}

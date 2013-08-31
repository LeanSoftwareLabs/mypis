package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.mypis.domain.GLAccount;
import com.leansoftwarelabs.mypis.service.GLAccountFacadeBean;

import java.util.Map;

import javax.annotation.PostConstruct;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.adf.view.rich.context.AdfFacesContext;

public class GLAccountEntryForm {
    private GLAccountFacadeBean service;
    private GLAccount account;

    @PostConstruct
    public void init(){
        
    }
    
    public GLAccountFacadeBean getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (GLAccountFacadeBean) context.lookup("java:comp/env/ejb/local/GLAccountFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }

    public GLAccount getAccount() {
        if(account == null){
            Integer accountId = (Integer) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("accountId");
            if(accountId == -1){
                account = new GLAccount();
            }else{
                account = getService().find(accountId);
            }
        }
        return account;
    }
    
    public String edit() {
        FormUtils.editing(true);
        return null;
    }

    public String cancel() {
        FormUtils.editing(false);
        if (account.getAccountId() == null) {
            return "done";
        } else {
            account = null;//reset
        }
        return null;
    }


    public String save() {
        this.account = getService().mergeEntity(this.account);
        FormUtils.editing(false);
        return null;
    }
    
    public Map getAccountTypeItems(){
        return GLAccount.AccountType.VALUES;
    }
}

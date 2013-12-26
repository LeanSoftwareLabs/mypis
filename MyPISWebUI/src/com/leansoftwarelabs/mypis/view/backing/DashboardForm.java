package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.ServiceProvider;
import com.leansoftwarelabs.mypis.domain.GLAccount;
import com.leansoftwarelabs.mypis.service.GLAccountFacadeBean;
import com.leansoftwarelabs.mypis.service.GLEntryFacadeBean;
import com.leansoftwarelabs.mypis.view.util.FormUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.model.SelectItem;

public class DashboardForm {
    private List<Object[]> bankCashData;
    
    public DashboardForm() {
        super();
    }
    
    @PostConstruct
    public void init(){
        bankCashData = new ArrayList<Object[]>();
        ServiceProvider<GLAccountFacadeBean> glAccountSP = FormUtils.getService("glAccountFacade");
        ServiceProvider<GLEntryFacadeBean> glEntrySP = FormUtils.getService("glEntryFacade");
        List<GLAccount> bankCashAccounts = glAccountSP.getService().findCashAndBankAccounts();
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.add(Calendar.DATE, -10);
        Date startDate = cal.getTime();
        for (GLAccount account : bankCashAccounts) {
            Object[] objArray = new Object[2];
            objArray[0] = account;
            objArray[1] = glEntrySP.getService().getDailyAccountBalanceByDateRange(account.getAccountId(), startDate, endDate);
            bankCashData.add(objArray);
        }
    }

    public List<Object[]> getBankCashData() {
        return bankCashData;
    }

}

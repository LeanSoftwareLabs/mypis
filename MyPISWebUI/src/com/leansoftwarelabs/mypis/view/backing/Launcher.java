package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.realm.domain.User;
import com.leansoftwarelabs.view.dynamicShell.TabContext;

import java.util.HashMap;
import java.util.Map;

import java.util.Random;

import javax.faces.event.ActionEvent;

import org.apache.shiro.SecurityUtils;

public class Launcher {
    
    public void launchBaptismalRegister(ActionEvent e){
        launch("Baptismal Register", "/WEB-INF/baptismal-register-list.xml#baptismal-register-list", null, false);
        
    }
    
    public void launchDeathRegister(ActionEvent e){
        launch("Death Register", "/WEB-INF/death-register-list.xml#death-register-list", null, false);
        
    }
    
    public void launchConfirmationRegister(ActionEvent actionEvent) {

        launch("Comfirmation Register", "/WEB-INF/confirmation-register-list.xml#confirmation-register-list", null,false);
    }
    
    public void launchUserRolesAdministration(ActionEvent actionEvent) {
        launch("Manage Users", "/WEB-INF/taskflows/admin/user-roles.xml#user-roles", null,false);
    }
    
    public void launchRolesPermissions(ActionEvent actionEvent) {
        launch("Manage Roles/Permissions", "/WEB-INF/taskflows/admin/roles-permissions.xml#roles-permissions", null,false);
    }
    
    
    public static void launch(String name, String taskflowId,
                              Map<String, Object> parameterMap,
                              Boolean multipleInstance) {
        TabContext.getCurrentInstance().launchActivity(name, taskflowId, parameterMap, multipleInstance);
        
    }

    public void launchTenantInformation(ActionEvent actionEvent) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        parameters.put("tenantId", currentUser.getTenant().getTenantId());
        System.out.println("tenantId: " +currentUser.getTenant().getTenantId());
        launch("Parish Information", "/WEB-INF/taskflows/admin/tenant-information.xml#tenant-information", parameters,true);
    }
    
    public void launchGLAccountList(ActionEvent actionEvent) {
        launch("GL Accounts","/WEB-INF/taskflows/acctg/gl-account-list.xml#gl-account-list", null,false);
    }
    
    public void launchContactList(ActionEvent actionEvent) {
        launch("Manage Contacts","/WEB-INF/taskflows/acctg/contact-list.xml#contact-list", null,false);
    }
    public void launchMarriageRegister(ActionEvent actionEvent) {
        // Add event code here...
    }

    public void launchGeneralLedger(ActionEvent actionEvent) {
        launch("General Ledger", "/WEB-INF/taskflows/acctg/general-ledger.xml#general-ledger", null,false);
    }
    
    public void launchSubAccounts(ActionEvent actionEvent) {
        launch("Subsidiary Accounts", "/WEB-INF/taskflows/acctg/sub-account-list.xml#sub-account-list", null,false);
    }
    
    public void launchTrackingCategories(ActionEvent actionEvent) {
        launch("Tracking", "/WEB-INF/taskflows/acctg/tracking-category.xml#tracking-category", null,false);
    }
    
    public void launchTrialBalance(ActionEvent actionEvent) {
        launch("Trial Balance", "/WEB-INF/taskflows/acctg/trial-balance.xml#trial-balance", null,false);
    }
    
    public void launchBalanceSheet(ActionEvent actionEvent) {
        launch("Balance Sheet", "/WEB-INF/taskflows/acctg/balance-sheet.xml#balance-sheet", null,false);
    }
    
    public void launchIncomeStatement(ActionEvent actionEvent) {
        launch("Income Statement", "/WEB-INF/taskflows/acctg/income-statement.xml#income-statement", null,false);
    }
}

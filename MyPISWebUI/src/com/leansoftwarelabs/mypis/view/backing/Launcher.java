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
}

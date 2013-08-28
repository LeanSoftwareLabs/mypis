package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.view.dynamicShell.TabContext;

import java.util.HashMap;
import java.util.Map;

import java.util.Random;

import javax.faces.event.ActionEvent;

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

  
   
}

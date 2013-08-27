package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.view.dynamicShell.TabContext;

import java.util.Map;

import javax.faces.event.ActionEvent;

public class Launcher {
    
    public void launchBaptismalRegister(ActionEvent e){
        launch("Baptismal Register", "/WEB-INF/baptismal-register-list.xml#baptismal-register-list", null, false);
        
    }
    
    public void launchConfirmationRegister(ActionEvent actionEvent) {
        // Add event code here...
        launch("Comfirmation Register", "/WEB-INF/confirmation-register-list.xml#confirmation-register-list", null,false);
    }
    
    
    public static void launch(String name, String taskflowId,
                              Map<String, Object> parameterMap,
                              Boolean multipleInstance) {
        TabContext.getCurrentInstance().launchActivity(name, taskflowId, parameterMap, false);
        
    }

  
   
}

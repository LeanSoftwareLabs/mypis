package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.view.dynamicShell.TabContext;
import com.leansoftwarelabs.view.utils.ADFUtils;
import com.leansoftwarelabs.view.utils.JSFUtils;

import java.util.HashMap;
import java.util.Map;

import javax.faces.event.ActionEvent;

public class Launcher {
    
    public void launchBaptismalRegister(ActionEvent e){
        launch("Baptismal Register", "/WEB-INF/baptismal-register-list.xml#baptismal-register-list", null, false);
    }
    
    
    public static void launch(String name, String taskflowId,
                              Map<String, Object> parameterMap,
                              Boolean multipleInstance) {
        TabContext.getCurrentInstance().launchActivity(name, taskflowId, parameterMap, false);
        
    }

}

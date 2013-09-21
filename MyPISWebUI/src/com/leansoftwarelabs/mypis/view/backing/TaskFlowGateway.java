package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.mypis.view.util.FormUtils;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowId;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.shiro.SecurityUtils;

public class TaskFlowGateway {
    public String route() {
        TaskFlowId taskFlowId =
            ControllerContext.getInstance().getCurrentViewPort().getTaskFlowContext().getTaskFlowId();
        System.out.println(taskFlowId.getFullyQualifiedName());
        if (!SecurityUtils.getSubject().isPermitted(taskFlowId.getFullyQualifiedName() + ":read")) {
            return "unauthorized";
        }
        Boolean editMode = (Boolean) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("editMode");
        if (editMode != null && editMode){
            editMode = SecurityUtils.getSubject().isPermitted(taskFlowId.getFullyQualifiedName() + ":update");
            FormUtils.editing(editMode);
        }
        return "gatePassed";
    }
}

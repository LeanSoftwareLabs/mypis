package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.mypis.service.ServiceException;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.validation.ConstraintViolation;

public final class FormUtils {
    public static void editing(boolean editMode) {
        ADFUtils.getPageFlowScope().put("editMode", editMode);
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("isDirty", editMode);
        raiseEvent("dirtyEvent", payload);
    }
    
    public static void raiseEvent(String eventName, Map<String, Object> payload) {
        EventHandler eventHandler = (EventHandler) ADFUtils.getPageFlowScope().get("eventHandler");
        if (eventHandler != null) {
            eventHandler.handleEvent(eventName, payload);
        }
    }
    
    public static void addFacesErrorMessage(Set violations) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : violations) {
            builder.append("\n");
            builder.append(((ConstraintViolation) obj).getMessage());
        }
        FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_ERROR, "Constraint Violation", builder.toString());
        FacesContext.getCurrentInstance().addMessage(null,message);
    }
    
    public static void addFacesErrorMessage(ServiceException se){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, se.getMessage(), "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}

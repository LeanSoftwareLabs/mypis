package com.leansoftwarelabs.mypis.view.util;

import com.leansoftwarelabs.ext.ServiceProvider;
import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.mypis.service.ServiceException;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.validation.ConstraintViolation;

import oracle.adf.view.rich.context.AdfFacesContext;

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
    
    public static ServiceProvider getService(String serviceName){
        Map services = (Map) AdfFacesContext.getCurrentInstance().getPageFlowScope().get("services");
        if(services == null){
            throw new RuntimeException("Parameter 'services' in pageFlowScope returned null.");
        }
        ServiceProvider service = (ServiceProvider) services.get(serviceName);
        if (service == null) {
            throw new RuntimeException("There are no service defined with name " + serviceName);
        }
        return service;
    }
    
    public static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
    
}

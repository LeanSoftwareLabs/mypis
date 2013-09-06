package com.leansoftwarelabs.mypis.view.backing;

import com.leansoftwarelabs.ext.adf.EventHandler;
import com.leansoftwarelabs.view.utils.ADFUtils;

import java.util.HashMap;
import java.util.Map;

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
}

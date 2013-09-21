package com.leansoftwarelabs.mypis.view.util;

import com.leansoftwarelabs.adf.query.AttributeDef;

import java.util.LinkedHashMap;
import java.util.Map;

public class AttributeDefMapProviderUtil {
    public static Map<String, AttributeDef> getGLAccountAttributeDefMap() {
        Map<String, AttributeDef> attributes = new LinkedHashMap<String, AttributeDef>();
        attributes.put("code", new AttributeDef("code", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("name", new AttributeDef("name", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("description", new AttributeDef("description", String.class, null, AttributeDef.INPUT_TEXT));
        attributes.put("dashboard", new AttributeDef("dashboard", Boolean.class, null, AttributeDef.CHECK_BOX));
        attributes.put("expenseClaims",
                       new AttributeDef("expenseClaims", Boolean.class, null, AttributeDef.CHECK_BOX));
        attributes.put("payments", new AttributeDef("payments", Boolean.class, null, AttributeDef.CHECK_BOX));
        return attributes;
    }
}

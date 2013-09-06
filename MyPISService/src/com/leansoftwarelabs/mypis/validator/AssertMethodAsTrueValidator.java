package com.leansoftwarelabs.mypis.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class AssertMethodAsTrueValidator implements ConstraintValidator<AssertMethodAsTrue, Object>{
    private static String TOKENIZER = "\\{(\\w+)\\}";
    private String methodName;
    private String objectHints;
    private String message;
    
    public void initialize(AssertMethodAsTrue assertMethodAsTrue) {
        methodName =  assertMethodAsTrue.value();
        objectHints = assertMethodAsTrue.objectHints();
        message = assertMethodAsTrue.message();
    }

    public boolean isValid(Object object,
                           ConstraintValidatorContext constraintValidatorContext) {
        

        try {
            Class clazz = object.getClass();
            Method validate = clazz.getMethod(methodName, new Class[0]);
            Boolean result = (Boolean) validate.invoke(object);
            if (!result){
                String processedObjectHints = processObjectHints(object, objectHints);
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message + " " + processedObjectHints).addConstraintViolation();
            }
            return result;
        } catch (Throwable e) {
            System.err.println(e);
        }

        return false;
    }
    
    private String processObjectHints(Object obj, String input) {
        Matcher m = Pattern.compile(TOKENIZER).matcher(input);
        StringBuffer sb = new StringBuffer(32);
        StringBuffer rsb =  new StringBuffer(10);
        while (m.find()) {
            rsb.replace(0, rsb.length(), evaluate(obj, m.group(1)));
            m.appendReplacement(sb, rsb.toString());
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    private String evaluate(Object obj, String token){
        Class clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField(token);
            field.setAccessible(true);
            return field.get(obj).toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return token;
        }
    }

}

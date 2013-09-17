package com.leansoftwarelabs.mypis.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationUtil {
    public static Validator getValidator(){
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    public static Set validate(Object object) {
        return getValidator().validate(object);
    }
    
}

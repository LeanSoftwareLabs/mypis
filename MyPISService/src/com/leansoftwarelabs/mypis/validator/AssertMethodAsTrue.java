package com.leansoftwarelabs.mypis.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {AssertMethodAsTrueValidator.class} )
@Documented

public @interface AssertMethodAsTrue {
    String message() default "method {value} returned false";
    String value() default "isValid";
    String objectHints() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    
}
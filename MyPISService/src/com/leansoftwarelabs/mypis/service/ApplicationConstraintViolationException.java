package com.leansoftwarelabs.mypis.service;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class ApplicationConstraintViolationException extends ServiceException{
    private Set<ConstraintViolation> violations;

    public void setViolations(Set<ConstraintViolation> violations) {
        this.violations = violations;
    }

    public Set<ConstraintViolation> getViolations() {
        return violations;
    }

    public ApplicationConstraintViolationException(Set<ConstraintViolation> violations){
        this.violations = violations;
    }
}

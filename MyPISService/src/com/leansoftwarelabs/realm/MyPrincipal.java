package com.leansoftwarelabs.realm;

import java.security.Principal;

import javax.enterprise.inject.Alternative;

import org.apache.shiro.SecurityUtils;

@Alternative
public class MyPrincipal implements Principal{
    public MyPrincipal() {
        super();
    }

    @Override
    public String getName() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }
}

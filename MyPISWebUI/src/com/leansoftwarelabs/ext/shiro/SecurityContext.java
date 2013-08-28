package com.leansoftwarelabs.ext.shiro;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public final class SecurityContext {
    
    private SecurityContext(){
        
    }
    public static boolean hasPermission(String permission){
        Subject currentUser =
            SecurityUtils.getSubject();
        return currentUser.isPermitted(permission);
    }
    
    public static boolean isUserInRole(String roleName){
        Subject currentUser =
            SecurityUtils.getSubject();
        return currentUser.hasRole(roleName);
    }
    public static boolean isUserInAllRoles(Collection<String> roleNames){
        Subject currentUser =
            SecurityUtils.getSubject();
        return currentUser.hasAllRoles(roleNames);
    }
    
    
    public static boolean isAuthenticated(){
        Subject currentUser =
            SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }
    
    public static String username(){
        return String.valueOf(SecurityUtils.getSubject().getPrincipal());
    }
    
}

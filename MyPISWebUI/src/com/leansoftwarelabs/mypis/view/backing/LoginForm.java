package com.leansoftwarelabs.mypis.view.backing;


import com.leansoftwarelabs.realm.domain.User;
import com.leansoftwarelabs.realm.service.UsersRolesPermissionsFacadeLocal;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.io.IOException;

import java.net.URL;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

public class LoginForm {
    private String userName;
    private String password;
    private boolean remember;
    public static final String HOME_URL = "/faces/pages/mypis_shell.jsf";
    public static final String LOGIN_URL = "/faces/login.jsf";
    private UsersRolesPermissionsFacadeLocal service;

    public LoginForm() {
        super();
    }

    public String login() {
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(userName, password, remember));
            Subject subject = SecurityUtils.getSubject();
            User user = getService().findUserByUsername((String) subject.getPrincipal());
            subject.getSession().setAttribute("user", user);
            HttpServletRequest request =
                (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
            SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
            //put user to session so user and tenant information can be accessed
            //anywhere on the page
            request.getSession().setAttribute("user", user);
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            if (savedRequest != null) {
                System.out.println("SavedRequest URL:" + savedRequest.getRequestUrl());
            }
            ctx.redirect(savedRequest != null ? savedRequest.getRequestUrl() : ctx.getRequestContextPath() + HOME_URL);
        } catch (Exception e) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, failed to validate you. Please try again", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return "";
    }
    
    public UsersRolesPermissionsFacadeLocal getService() {
        if (service == null) {
            try {
                final Context context = new InitialContext();
                service =
                    (UsersRolesPermissionsFacadeLocal) context.lookup("java:comp/env/ejb/local/UsersRolesPermissionsFacade");
            } catch (Exception ex) {
                //TODO : bubble up exception or put in log file.
                ex.printStackTrace();
            }
        }
        return service;
    }
    


    public void logout() throws IOException {
        SecurityUtils.getSubject().logout();
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        ctx.redirect(ctx.getRequestContextPath() + LOGIN_URL);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public boolean isRemember() {
        return remember;
    }
}

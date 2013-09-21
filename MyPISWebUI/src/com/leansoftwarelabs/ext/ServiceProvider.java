package com.leansoftwarelabs.ext;


import javax.naming.Context;
import javax.naming.InitialContext;

public class ServiceProvider<T> {
    private T service;
    private String jndiNamespace;

    public ServiceProvider(String jndiNamespace) {
        this.jndiNamespace = jndiNamespace;
    }
    
    public ServiceProvider(){}

    public T getService() {
        if (service == null) {
            if(jndiNamespace == null){
                throw new RuntimeException("jndiNamespace not initialized");
            }
            try {
                final Context context = new InitialContext();
                service = (T)context.lookup(jndiNamespace);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return service;
    }


    public void setJndiNamespace(String jndiNamespace) {
        this.jndiNamespace = jndiNamespace;
    }

    public String getJndiNamespace() {
        return jndiNamespace;
    }

}

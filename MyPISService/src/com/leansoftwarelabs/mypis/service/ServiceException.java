package com.leansoftwarelabs.mypis.service;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ServiceException extends Exception {
    public ServiceException(String string, Throwable throwable, boolean b, boolean b1) {
        super(string, throwable, b, b1);
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public ServiceException(String string) {
        super(string);
    }

    public ServiceException() {
        super();
    }
}

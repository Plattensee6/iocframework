package com.plattensee.iocframework.exception;

public class ImplementationNotFoundException extends RuntimeException{
    public ImplementationNotFoundException() {
    }

    public ImplementationNotFoundException(String message) {
        super(message);
    }

    public ImplementationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImplementationNotFoundException(Throwable cause) {
        super(cause);
    }

    public ImplementationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.plattensee.iocframework.exception;

public class WiredConstructorInvocationException extends RuntimeException{
    public WiredConstructorInvocationException() {
    }

    public WiredConstructorInvocationException(String message) {
        super(message);
    }

    public WiredConstructorInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WiredConstructorInvocationException(Throwable cause) {
        super(cause);
    }

    public WiredConstructorInvocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

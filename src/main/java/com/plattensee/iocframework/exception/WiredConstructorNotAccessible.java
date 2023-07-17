package com.plattensee.iocframework.exception;

public class WiredConstructorNotAccessible extends RuntimeException{
    public WiredConstructorNotAccessible() {
    }

    public WiredConstructorNotAccessible(String message) {
        super(message);
    }

    public WiredConstructorNotAccessible(String message, Throwable cause) {
        super(message, cause);
    }

    public WiredConstructorNotAccessible(Throwable cause) {
        super(cause);
    }

    public WiredConstructorNotAccessible(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

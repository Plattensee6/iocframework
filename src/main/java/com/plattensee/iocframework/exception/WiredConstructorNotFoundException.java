package com.plattensee.iocframework.exception;

public class WiredConstructorNotFoundException extends RuntimeException{
    public WiredConstructorNotFoundException() {
    }

    public WiredConstructorNotFoundException(String message) {
        super(message);
    }

    public WiredConstructorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WiredConstructorNotFoundException(Throwable cause) {
        super(cause);
    }

    public WiredConstructorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.plattensee.iocframework.exception;

public class UndefinedImplementationException extends RuntimeException{
    public UndefinedImplementationException() {
    }

    public UndefinedImplementationException(String message) {
        super(message);
    }

    public UndefinedImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UndefinedImplementationException(Throwable cause) {
        super(cause);
    }

    public UndefinedImplementationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

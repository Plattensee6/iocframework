package com.plattensee.iocframework.exception;

public class PackageScanningException extends RuntimeException{

    public PackageScanningException() {
    }

    public PackageScanningException(String message) {
        super(message);
    }

    public PackageScanningException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageScanningException(Throwable cause) {
        super(cause);
    }

    public PackageScanningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.wemakeprice.tour.bo.common.exception;

public class SecuredException extends RuntimeException {

    public SecuredException(String message) {
        super(message);
    }

    public SecuredException(String message, Throwable cause) {
        super(message, cause);
    }
}

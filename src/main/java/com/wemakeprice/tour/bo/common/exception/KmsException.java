package com.wemakeprice.tour.bo.common.exception;

public class KmsException extends RuntimeException {

    public KmsException(String message) {
        super(message);
    }

    public KmsException(Throwable cause) {
        super(cause);
    }
}

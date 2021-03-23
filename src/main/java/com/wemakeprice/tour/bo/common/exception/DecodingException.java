package com.wemakeprice.tour.bo.common.exception;

public class DecodingException extends Exception {

    public DecodingException(String message) {
        super(message);
    }

    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.wemakeprice.tour.bo.common.exception;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = -2066735373810365705L;

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

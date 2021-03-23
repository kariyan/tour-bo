package com.wemakeprice.tour.bo.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private ErrorType errorType;
    private String errorMessage;
    private String cause;
    private List<Object> errorObjectList;

    public String getErrorMessage() {
        return errorMessage == null ? errorType.getTitle() : errorMessage;
    }
}

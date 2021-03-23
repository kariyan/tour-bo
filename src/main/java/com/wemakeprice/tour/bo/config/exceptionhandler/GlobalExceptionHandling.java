package com.wemakeprice.tour.bo.config.exceptionhandler;

import com.wemakeprice.tour.bo.common.exception.ErrorMessage;
import com.wemakeprice.tour.bo.common.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = {IOException.class})
    protected ResponseEntity<Object> handleIOException(IOException ex) {
        if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(ex), "Broken pipe")) {
            // socket is closed, cannot return any response
            return null;
        }
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.UNDEFINED);

        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<Object> handleBadRequestError(HttpClientErrorException ex) {
        ErrorMessage errorMessage = new ErrorMessage();

        if (HttpStatus.UNAUTHORIZED == ex.getStatusCode()) {
            errorMessage.setErrorType(ErrorType.NO_LOGIN);
        } else {
            errorMessage.setErrorType(ErrorType.HTTP);
            errorMessage.setErrorMessage(" status : " + ex.getStatusText() + ", body : " + ex.getResponseBodyAsString());
            log.warn(ex.getMessage(), ex);
        }

        return new ResponseEntity<>(errorMessage, ex.getStatusCode());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.ILLEGAL_ARGUMENT);

        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BindException.class})
    protected ResponseEntity<Object> handleBindException(BindException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.ILLEGAL_ARGUMENT);
        StringBuilder sbErrors = new StringBuilder();

        for (FieldError fieldError : ex.getFieldErrors()) {
            sbErrors.append(fieldError.getDefaultMessage()).append(": [").append(fieldError.getField()).append("]<br/>");
        }

        errorMessage.setErrorMessage(sbErrors.toString());
        if (ex.getCause() != null) {
            errorMessage.setCause(ex.getCause().getMessage());
        }

        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    protected ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.ILLEGAL_ARGUMENT);
        StringBuilder sbErrors = new StringBuilder();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            sbErrors.append(fieldError.getDefaultMessage()).append(": [").append(fieldError.getField()).append("]<br/>");
        }

        errorMessage.setErrorMessage(sbErrors.toString());
        if (ex.getCause() != null) {
            errorMessage.setCause(ex.getCause().getMessage());
        }

        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ValidationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.ILLEGAL_ARGUMENT);

        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageConversionException.class})
    protected ResponseEntity<Object> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.ILLEGAL_ARGUMENT);

        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex, HttpServletResponse response) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.UNDEFINED);

        log.error(ex.getMessage(), ex);
        return processCORS(response, errorMessage);
    }

    private ResponseEntity<Object> processCORS(HttpServletResponse response, ErrorMessage errorMessage) {
        if (response.getHeader("Access-Control-Allow-Origin") == null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Access-Control-Allow-Origin", "*");
            return new ResponseEntity<>(errorMessage, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

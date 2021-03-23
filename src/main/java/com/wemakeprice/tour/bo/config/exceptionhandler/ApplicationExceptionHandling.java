package com.wemakeprice.tour.bo.config.exceptionhandler;

import com.wemakeprice.tour.bo.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ControllerAdvice
public class ApplicationExceptionHandling {

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.AUTHENTICATION_ERROR);
        errorMessage.setErrorMessage(ex.getMessage());

        log.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {AuthorizationException.class})
    protected ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.AUTHORITY_ERROR);
        errorMessage.setErrorMessage(ex.getMessage());

        log.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ExhibitionException.class})
    protected ResponseEntity<Object> handleExhibitionException(ExhibitionException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.EXHIBITION_ERROR);
        errorMessage.setErrorMessage(ex.getMessage());

        log.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorType(ErrorType.APPLICATION_ERROR);
        errorMessage.setErrorMessage(ex.getMessage());

        log.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}

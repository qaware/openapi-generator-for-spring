package de.qaware.openapigeneratorforspring.test.app32;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public App32Controller.ErrorDto handleAnotherException(App32Controller.AnotherException ex) {
        return null;
    }
}

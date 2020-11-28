package de.qaware.openapigeneratorforspring.test.app32;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App32Controller {

    @GetMapping(value = "/mapping1")
    public void mapping1_throwingSomeException() throws SomeException {

    }

    @GetMapping(value = "/mapping2")
    public void mapping2_throwingAnotherException() throws AnotherException {

    }

    @Value
    static class ErrorDto {
        @Schema(description = "Error code, machine-readable")
        String errorCode;
        @Schema(description = "Error description, human-readable")
        String errorDescription;
    }

    private static class SomeException extends Exception {

    }

    static class AnotherException extends Exception {

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleSomeException(SomeException ex) {
        return null;
    }
}

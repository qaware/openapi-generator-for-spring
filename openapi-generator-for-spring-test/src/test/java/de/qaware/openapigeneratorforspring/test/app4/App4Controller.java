package de.qaware.openapigeneratorforspring.test.app4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseStatus(HttpStatus.CREATED)
public class App4Controller extends App4BaseController {

    @GetMapping("/get1")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "310", description = "Some more responses 1"),
            @ApiResponse(responseCode = "311", description = "Some more responses 2"),
    })
    public String getMapping1() {
        return null;
    }


    @GetMapping(value = "/get2", produces = "text/plain")
    @ApiResponse(description = "Custom default description for response")
    @ApiResponse(responseCode = "210", description = "All ok overwrite base")
    @Operation(responses = {
            @ApiResponse(description = "Custom default description for response which actually matters"),
            @ApiResponse(responseCode = "410", description = "Some response overwrite base base")
    })
    public String getMapping2() {
        return null;
    }
}

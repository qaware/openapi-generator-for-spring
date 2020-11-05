package de.qaware.openapigeneratorforspring.test.app4;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class App4SimpleController {
    @PostMapping(value = "/post1", produces = "text/plain")
    @ApiResponse(description = "Default response for post mapping")
    @Schema(implementation = String.class)
    public List<String> postMapping1() {
        return null;
    }
}

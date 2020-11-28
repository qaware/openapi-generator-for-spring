package de.qaware.openapigeneratorforspring.test.app30;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App30Controller {

    @GetMapping(value = "/mapping1", produces = MediaType.TEXT_HTML_VALUE)
    public String mapping1_textHtml() {
        return null;
    }

    @GetMapping(value = "/mapping1")
    public void mapping1() {

    }

    @PostMapping(value = "/mapping1", consumes = MediaType.TEXT_HTML_VALUE)
    public void mapping2_textHtml(@RequestBody String param1) {

    }

    @PostMapping(value = "/mapping1")
    public void mapping2() {

    }

    @PostMapping(value = "/mapping3", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    @ApiResponse(description = "some description", content = @Content(schema = @Schema(implementation = String.class)))
    public void mapping3_textHtml(@RequestBody String param1) {

    }

    @PostMapping(value = "/mapping3")
    public void mapping3() {

    }
}

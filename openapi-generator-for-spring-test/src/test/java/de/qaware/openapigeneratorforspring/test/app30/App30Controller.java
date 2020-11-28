package de.qaware.openapigeneratorforspring.test.app30;

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
}

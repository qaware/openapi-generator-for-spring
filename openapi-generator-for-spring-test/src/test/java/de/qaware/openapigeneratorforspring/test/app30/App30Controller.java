package de.qaware.openapigeneratorforspring.test.app30;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App30Controller {

    @PostMapping(value = "/mapping1", consumes = MediaType.TEXT_HTML_VALUE)
    public void mapping1(@RequestBody String string) {

    }

    @PostMapping(value = "/mapping1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void mapping2(@RequestBody String string) {

    }
}

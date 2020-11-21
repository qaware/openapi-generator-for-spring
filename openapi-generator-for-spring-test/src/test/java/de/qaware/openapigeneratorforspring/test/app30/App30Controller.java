package de.qaware.openapigeneratorforspring.test.app30;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App30Controller {

    @PostMapping(value = "/mapping1", produces = MediaType.TEXT_HTML_VALUE)
    public void mapping1() {

    }

    @PostMapping(value = "/mapping1")
    public void mapping2() {

    }
}

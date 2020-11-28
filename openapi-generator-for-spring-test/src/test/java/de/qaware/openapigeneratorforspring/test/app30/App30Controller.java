package de.qaware.openapigeneratorforspring.test.app30;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App30Controller {


    @PostMapping(value = "/mapping1", consumes = MediaType.TEXT_HTML_VALUE)
    public void mapping1_html(@RequestBody String html) {

    }

    @PostMapping(value = "/mapping1", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void mapping1_plainText(@RequestBody String plainText) {

    }

    @GetMapping(value = "/mapping2", produces = MediaType.TEXT_HTML_VALUE)
    public String mapping2_html() {
        return null;
    }

    @GetMapping(value = "/mapping2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String mapping2_plainText() {
        return null;
    }

    @GetMapping(value = "/mapping3", produces = MediaType.TEXT_HTML_VALUE)
    public String mapping3_get_textHtml() {
        return null;
    }

    @GetMapping(value = "/mapping3")
    public void mapping3_get() {

    }

    @PostMapping(value = "/mapping3", consumes = MediaType.TEXT_HTML_VALUE)
    public void mapping3_post_textHtml(@RequestBody String param1) {

    }

    @PostMapping(value = "/mapping3")
    public void mapping3_post() {

    }
}

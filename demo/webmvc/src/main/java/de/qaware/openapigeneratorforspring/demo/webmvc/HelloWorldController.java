package de.qaware.openapigeneratorforspring.demo.webmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping(path = "hello-world")
public class HelloWorldController {
    @GetMapping(value = "greeting", produces = TEXT_PLAIN_VALUE)
    public String getGreeting() {
        return "Hello from the demo app!";
    }
}

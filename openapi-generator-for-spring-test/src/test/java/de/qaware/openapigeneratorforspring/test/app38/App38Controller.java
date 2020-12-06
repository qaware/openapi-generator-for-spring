package de.qaware.openapigeneratorforspring.test.app38;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App38Controller {
    @GetMapping("mapping1")
    public String mapping1(@RequestBody String request, @RequestParam String param1) {
        return null;
    }
}

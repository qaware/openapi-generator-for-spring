package de.qaware.openapigeneratorforspring.test.app9;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = "text/plain")
public class App9Controller {
    @GetMapping("get-1")
    public void getMapping1(@RequestParam String param1, @RequestParam String param2, @RequestBody SomeDto someDto) {

    }

    @GetMapping(value = "get-2")
    public void getMapping2(@RequestParam String param1, @RequestParam String param2, @RequestParam SomeDto param3, @RequestBody String requestBody) {

    }

    @Value
    private static class SomeDto {
        String param1;
        String param2;
    }
}

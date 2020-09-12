package de.qaware.openapigeneratorforspring.test.app9;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class App9Controller {
    @GetMapping("get-1")
    public void getMapping1(@RequestParam String param1, @RequestParam String param2) {

    }

    @GetMapping("get-2")
    public void getMapping2(@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {

    }
}

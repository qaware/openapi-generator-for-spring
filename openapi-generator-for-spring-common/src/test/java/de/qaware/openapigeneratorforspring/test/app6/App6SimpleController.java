package de.qaware.openapigeneratorforspring.test.app6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "simple")
public class App6SimpleController {

    @GetMapping("get-mapping")
    public String getMappingRequestParam(@RequestParam String param1) {
        return null;
    }
}


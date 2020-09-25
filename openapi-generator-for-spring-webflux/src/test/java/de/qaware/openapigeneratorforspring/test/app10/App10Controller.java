package de.qaware.openapigeneratorforspring.test.app10;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App10Controller {
    @GetMapping("admin/mapping-1")
    public String adminMapping1() {
        return null;
    }

    @GetMapping("user/mapping-1")
    public String userMapping1() {
        return null;
    }
}

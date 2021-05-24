package de.qaware.openapigeneratorforspring.test.app18;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class App18Controller {
    @GetMapping("admin/mapping-1")
    public void adminMapping1() {

    }

    @GetMapping("user/mapping-1")
    public void userMapping1() {

    }
}

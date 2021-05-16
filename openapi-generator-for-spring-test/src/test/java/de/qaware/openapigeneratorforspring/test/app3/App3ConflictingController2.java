package de.qaware.openapigeneratorforspring.test.app3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v2")
class App3ConflictingController2 {

    @GetMapping("get-mapping")
    public String getMappingReturnString() {
        return null;
    }

}


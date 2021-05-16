package de.qaware.openapigeneratorforspring.test.app3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1")
class App3ConflictingController1 {

    @GetMapping("get-mapping")
    public String getMappingReturnString() {
        return null;
    }

    @PostMapping("post-mapping")
    public void postMapping() {

    }

}
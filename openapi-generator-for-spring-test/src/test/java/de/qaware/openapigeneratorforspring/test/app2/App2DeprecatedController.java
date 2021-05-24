package de.qaware.openapigeneratorforspring.test.app2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "all-deprecated")
@Deprecated
class App2DeprecatedController {

    @GetMapping("get-mapping")
    public String allDeprecatedGetMapping() {
        return null;
    }

    @PostMapping("post-mapping")
    public void allDeprecatedPostMapping() {
    }
}


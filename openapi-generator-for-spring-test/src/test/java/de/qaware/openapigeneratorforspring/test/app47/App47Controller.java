package de.qaware.openapigeneratorforspring.test.app47;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RestController
class App47Controller {

    @GetMapping("/mapping1")
    public HashSet<String> mapping1() {
        return null;
    }

    @GetMapping("/mapping2")
    public List<String> mapping2(@RequestParam Collection<String> param1) {
        return null;
    }

    @GetMapping("/mapping3")
    @ArraySchema(uniqueItems = true)
    public List<String> mapping3(@ArraySchema(uniqueItems = true) @RequestParam Collection<String> param2) {
        return null;
    }
}

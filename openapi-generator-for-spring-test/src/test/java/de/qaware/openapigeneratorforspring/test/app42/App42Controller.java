package de.qaware.openapigeneratorforspring.test.app42;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App42Controller {

    @GetMapping
    public SomeType1 mapping1() {
        return null;
    }

    @Value
    private static class SomeType1 {
        @MyRequiredAnnotation
        String notNullProperty;
    }

}

package de.qaware.openapigeneratorforspring.test.app55;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App55Controller {

    @GetMapping
    public SomeDto getJson() {
        return new SomeDto("some-value");
    }

    @Value
    private static class SomeDto {
        String someProperty;
    }
}

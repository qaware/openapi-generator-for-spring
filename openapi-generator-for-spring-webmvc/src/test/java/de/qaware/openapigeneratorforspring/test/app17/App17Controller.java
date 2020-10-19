package de.qaware.openapigeneratorforspring.test.app17;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class App17Controller {

    @GetMapping("/mapping1")
    public List<SomeDto> mapping1(@RequestBody SomeDto someDto) {
        return null;
    }

    @Value
    private static class SomeDto {
        String stringProperty1;
    }
}

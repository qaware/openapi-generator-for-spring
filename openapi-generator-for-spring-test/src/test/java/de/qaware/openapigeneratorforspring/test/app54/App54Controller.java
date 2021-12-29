package de.qaware.openapigeneratorforspring.test.app54;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
class App54Controller {
    @GetMapping("mapping1")
    public Animal mapping1() {
        return null;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Dog.class, name = "DOG"),
            @JsonSubTypes.Type(value = Cat.class, name = "CAT"),
    })
    private interface Animal {

    }

    @Value
    private static class Dog {
        String wuff;
    }

    @Value
    private static class Cat {
        String meow;
    }
}

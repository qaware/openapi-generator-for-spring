package de.qaware.openapigeneratorforspring.test.app54;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Dog.class, name = "DOG"),
            @JsonSubTypes.Type(value = Cat.class, name = "CAT"),
    })
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    private abstract static class Animal {
        private final String type;
    }

    private static class Dog extends Animal {
        private Dog() {
            super("DOG");
        }
    }

    private static class Cat extends Animal {
        private Cat() {
            super("CAT");
        }
    }
}

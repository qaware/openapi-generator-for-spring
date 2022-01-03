package de.qaware.openapigeneratorforspring.test.app103;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
class App103Controller {
    @GetMapping("mapping1")
    public Animal mapping1() {
        return null;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Dog.class, name = "DOG"),
            @JsonSubTypes.Type(value = Cat.class, name = "CAT"),
    })
    private abstract static class Animal {
        private final String type;

        protected Animal(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    @Value
    private static class Dog extends Animal {
        private Dog() {
            super("DOG");
        }
    }

    @Value
    private static class Cat extends Animal {
        private Cat() {
            super("CAT");
        }
    }
}

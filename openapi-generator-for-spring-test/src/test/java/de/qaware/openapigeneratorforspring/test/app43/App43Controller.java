package de.qaware.openapigeneratorforspring.test.app43;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App43Controller {

    @GetMapping
    public Shape mapping1() {
        return null;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Rectangle.class, name = "RECTANGLE"),
            @JsonSubTypes.Type(value = Circle.class, name = "RATING")
    })
    private interface Shape {
    }

    @Value
    private static class Rectangle implements Shape {
        String name;
        int area;
    }

    @Value
    private static class Circle implements Shape {
        String name;
        int radius;
    }

}

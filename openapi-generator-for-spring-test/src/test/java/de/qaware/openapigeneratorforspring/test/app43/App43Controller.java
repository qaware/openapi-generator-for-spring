package de.qaware.openapigeneratorforspring.test.app43;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App43Controller {

    @GetMapping
    public Shape mapping1() {
        return new Rectangle("squary", 42);
    }

    @PostMapping
    public void mapping2(@RequestBody ExtendableBody body) {

    }

    @PutMapping
    public void mapping3(@RequestBody AbstractBody body) {

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Rectangle.class, name = "RECTANGLE"),
            @JsonSubTypes.Type(value = Circle.class, name = "RATING")
    })
    private interface Shape {
    }

    @Value
    @Schema(description = "Not quite a square")
    private static class Rectangle implements Shape {
        String name;
        int area;
    }

    @Value
    @Schema(description = "So round")
    private static class Circle implements Shape {
        String name;
        int radius;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ExtendableBody.class),
            @JsonSubTypes.Type(value = ExtendedBody.class)
    })
    @Setter
    private static class ExtendableBody {
        String property1;
    }

    @Setter
    private static class ExtendedBody extends ExtendableBody {
        String property2;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ConcreteBody.class),
    })
    @Schema(title = "The Title!")
    private interface BodyInterface {
        String getId();
    }

    @Getter
    @Setter
    @JsonSubTypes({
            @JsonSubTypes.Type(value = AnotherBody.class),
    })
    private static abstract class AbstractBody implements BodyInterface {
        String id;
    }

    @Getter
    private static class ConcreteBody extends AbstractBody {
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String readOnlyProperty;
    }

    @Getter
    @Setter
    @Schema(description = "Another body with property3")
    private static class AnotherBody extends AbstractBody {
        String property3;
    }
}

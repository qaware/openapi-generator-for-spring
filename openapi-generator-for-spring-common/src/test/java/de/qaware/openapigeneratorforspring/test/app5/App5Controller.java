package de.qaware.openapigeneratorforspring.test.app5;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

@RestController
public class App5Controller {

    @GetMapping("/get1")
    public String getMappingWithString() {
        return null;
    }

    @GetMapping("/get2")
    public SimpleDto getMappingWithSimpleDto() {
        return null;
    }

    @GetMapping("/get3")
    public ComplexDto getMappingWithComplexDto() {
        return null;
    }

    @Schema(description = "global description", title = "should never appear")
    private interface BaseForEverything {

    }

    @Schema(title = "global title")
    private interface BaseForDto extends BaseForEverything {

    }

    @Value
    private static class SimpleDto implements BaseForDto {
        @Nullable
        String property1;
    }

    @Value
    private static class ComplexDto implements BaseForDto {
        @Schema(description = "description1")
        ComplexDto other1;
        @Schema(title = "title override", description = "description2")
        ComplexDto other2;
        @Nullable
        SomeOtherDto someOtherDto;
    }

    @Value
    private static class SomeOtherDto {
        @Schema(description = "nestedDescription")
        ComplexDto other;
        @Schema(description = "nestedOtherDescription")
        SomeOtherDto someOtherDto;
    }
}

package de.qaware.openapigeneratorforspring.test.app34;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class App34Controller {

    @GetMapping(value = "/mapping1")
    public SomeListDto mapping1() {
        return null;
    }

    @GetMapping(value = "/mapping2")
    public SomeDto mapping2() {
        return null;
    }

    @Value
    @Schema(description = "Description of SomeListDto")
    static class SomeListDto {
        @Schema(description = "List of entries", required = true)
        @ArraySchema(schema = @Schema(deprecated = true))
        List<SomeDto> entries;
    }

    @Value
    @Schema(description = "Description of SomeDto")
    static class SomeDto {
        @Schema(description = "Description for some property")
        String someProperty1;
    }
}

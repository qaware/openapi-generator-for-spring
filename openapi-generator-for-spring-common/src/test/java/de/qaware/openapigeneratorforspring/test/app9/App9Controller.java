package de.qaware.openapigeneratorforspring.test.app9;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = "text/plain")
public class App9Controller {
    @GetMapping("mapping-1")
    public void mapping1(@RequestParam String param1, @RequestParam String param2, @RequestBody SomeDeprecatedDto someDeprecatedDto) {

    }

    @GetMapping(value = "mapping-2")
    public void mapping2(@RequestParam String param1, @RequestParam String param2, @RequestParam SomeDeprecatedDto param3, @RequestBody String requestBody) {

    }

    @PostMapping(value = "mapping-3", consumes = "application/json")
    public void mapping3(@RequestBody(required = false) String requestBody) {

    }

    @PutMapping(value = "mapping-4", consumes = "application/json")
    public void mapping4(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Some body description",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SomeDeprecatedDto.class))
            )
            @RequestBody String requestBody
    ) {

    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Some other body description",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SomeDeprecatedDto.class))
            )
    )
    @PutMapping(value = "mapping-5", consumes = "application/json")
    public void mapping5(@RequestBody String requestBody) {

    }

    @GetMapping(value = "mapping-6")
    public void mapping6(@RequestParam String param1, @RequestParam String param2,
                         @Schema(name = "App9Controller.SomeDeprecatedDto") @RequestParam @Deprecated SomeDto param3,
                         @Schema(name = "App9Controller.SomeDeprecatedDto") @RequestBody @Deprecated SomeDto requestBody) {

    }

    private static class SomeDto {
        String param1;
        String param2;
    }

    @SuppressWarnings("DeprecatedIsStillUsed") // deprecated for test
    @Deprecated
    private static class SomeDeprecatedDto {
        String param1;
        String param2;
    }
}

package de.qaware.openapigeneratorforspring.test.app20;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
class App20Controller {
    @GetMapping(value = "/mapping-1", produces = "text/plain")
    public Mono<String> mapping1() {
        return null;
    }

    @PutMapping("/mapping-2")
    @ArraySchema(arraySchema = @Schema(description = "Many SomeDtos"))
    public Flux<SomeDto> mapping2() {
        return null;
    }

    @PostMapping(value = "/mapping-3")
    public Mono<SomeDto> mapping3() {
        return null;
    }

    @PostMapping(value = "/mapping-4", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SomeDto> mapping4(@RequestBody @Schema(description = "request body DTO") SomeDto someDto) {
        return null;
    }

    @PutMapping("/mapping-5")
    public Flux<List<Instant>> mapping5() {
        return null;
    }

    @Schema(description = "Some DTO description")
    @Value
    private static class SomeDto {
        String stringProperty1;
        boolean booleanProperty2;
    }
}

package de.qaware.openapigeneratorforspring.test.app48;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@ArraySchema(minItems = 4, arraySchema = @Schema(description = "Description for array schema"))
class App48Controller {

    @GetMapping("/mapping-1")
    @ArraySchema(maxItems = 10)
    public Flux<ListLikeDto> mapping1() {
        return null;
    }

    @ArraySchema(minItems = 5, maxItems = 9, schema = @Schema(description = "Description for ListLikeDto items"))
    private static class ListLikeDto extends ArrayList<String> {
    }
}

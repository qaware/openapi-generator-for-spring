package de.qaware.openapigeneratorforspring.test.app48;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@ArraySchema(minItems = 4, arraySchema = @Schema(description = "Description for array schema"))
class App48Controller {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ArraySchema(maxItems = 10)
    public Flux<ListLikeDto> mapping1() {
        return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public RecursiveListDto mapping2(@RequestBody RecursiveListDto requestBody) {
        return requestBody;
    }

    @GetMapping(produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public Flux<RecursiveListDto> mapping3() {
        return null;
    }

    @ArraySchema(minItems = 5, maxItems = 9, schema = @Schema(description = "Description for ListLikeDto items"))
    private static class ListLikeDto extends ArrayList<String> {

    }

    @ArraySchema(
            arraySchema = @Schema(description = "Description for RecursiveListDto", example = "[[[[[[]]]]], null, [null]]"),
            schema = @Schema(description = "Description for RecursiveListDto items")
    )
    private static class RecursiveListDto extends ArrayList<RecursiveListDto> {
        @JsonCreator
        public RecursiveListDto(Collection<?> c) {
            super(c.stream()
                    .map(RecursiveListDto::castInnerList)
                    .collect(Collectors.toList())
            );
        }

        private static RecursiveListDto castInnerList(Object i) {
            return i instanceof Collection ? new RecursiveListDto((Collection<?>) i) : null;
        }
    }
}

package de.qaware.openapigeneratorforspring.test.app45;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

@RestController
class App45Controller {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Base1 mapping1(@RequestBody NestedBodyClass requestBody) {
        return new NestedClass(new NestedClass(requestBody));
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = NestedClass.class, name = "nested"),
            @JsonSubTypes.Type(value = NestedBodyClass.class, name = "nestedBody"),
    })
    private interface Base1 {

    }

    @Value
    private static class NestedClass implements Base1 {
        @Nullable
        Base1 nestedProperty;
    }

    @Value
    @Jacksonized
    @Builder
    private static class NestedBodyClass implements Base1 {
        @Nullable
        Base1 nestedProperty;
    }
}

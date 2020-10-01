package de.qaware.openapigeneratorforspring.model.header;

import com.fasterxml.jackson.annotation.JsonValue;
import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Header
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#headerObject"
 */
@Data
public class Header implements HasExtensions {
    private String description;
    private String $ref;
    private Boolean required;
    private Boolean deprecated;
    private StyleEnum style;
    private Boolean explode;
    private Schema schema;
    private Map<String, Example> examples;
    private Object example;
    private Content content;
    private Map<String, Object> extensions;

    @RequiredArgsConstructor
    public enum StyleEnum {
        SIMPLE("simple");

        @JsonValue
        @Getter
        private final String value;
    }
}


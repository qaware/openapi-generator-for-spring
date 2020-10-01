package de.qaware.openapigeneratorforspring.model.media;


import com.fasterxml.jackson.annotation.JsonValue;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.header.Header;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Encoding
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#encodingObject"
 */

@Data
public class Encoding implements HasExtensions {
    private String contentType;
    private Map<String, Header> headers;
    private Style style;
    private Boolean explode;
    private Boolean allowReserved;
    private Map<String, Object> extensions;

    @RequiredArgsConstructor
    public enum Style {
        FORM("form"),
        SPACE_DELIMITED("spaceDelimited"),
        PIPE_DELIMITED("pipeDelimited"),
        DEEP_OBJECT("deepObject");

        @JsonValue
        @Getter
        private final String value;
    }
}

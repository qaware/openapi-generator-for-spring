package de.qaware.openapigeneratorforspring.model.header;

import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import de.qaware.openapigeneratorforspring.model.reference.HasReference;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Header
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#headerObject"
 */
@Data
@Builder
public class Header implements HasExtensions, HasReference {
    private String description;
    private String ref;
    private Boolean required;
    private Boolean deprecated;
    private String style;
    private Boolean explode;
    private Schema schema;
    private Map<String, Example> examples;
    private Object example;
    private Content content;
    private Map<String, Object> extensions;
}


package de.qaware.openapigeneratorforspring.model.media;

import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * MediaType
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#mediaTypeObject"
 */
@Data
public class MediaType implements HasExtensions {
    private Schema schema;
    private Map<String, Example> examples;
    private Object example;
    private Map<String, Encoding> encoding;
    private Map<String, Object> extensions;
}


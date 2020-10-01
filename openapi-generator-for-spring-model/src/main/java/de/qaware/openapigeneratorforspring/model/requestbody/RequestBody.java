package de.qaware.openapigeneratorforspring.model.requestbody;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.media.Content;
import lombok.Data;

import java.util.Map;

/**
 * RequestBody
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#requestBodyObject"
 */

@Data
public class RequestBody implements HasExtensions {
    private String description;
    private Content content;
    private Boolean required;
    private Map<String, Object> extensions;
    private String $ref;
}


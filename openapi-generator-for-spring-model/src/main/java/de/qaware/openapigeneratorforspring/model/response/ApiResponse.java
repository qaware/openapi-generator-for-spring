package de.qaware.openapigeneratorforspring.model.response;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.header.Header;
import de.qaware.openapigeneratorforspring.model.link.Link;
import de.qaware.openapigeneratorforspring.model.media.Content;
import lombok.Data;

import java.util.Map;

/**
 * ApiResponse
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#responseObject"
 */
@Data
public class ApiResponse implements HasExtensions {
    private String description;
    private Map<String, Header> headers;
    private Content content;
    private Map<String, Link> links;
    private Map<String, Object> extensions;
    private String $ref;
}


package de.qaware.openapigeneratorforspring.model.link;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.header.Header;
import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.Data;

import java.util.Map;

/**
 * Link
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#linkObject"
 */
@Data
public class Link implements HasExtensions {
    private String operationRef;
    private String operationId;
    private Map<String, String> parameters;
    private Object requestBody;
    private Map<String, Header> headers;
    private String description;
    private String $ref;
    private Map<String, Object> extensions;
    private Server server;
}


package de.qaware.openapigeneratorforspring.model.link;

import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * Link
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#linkObject"
 */
@Data
public class Link implements HasExtensions, HasReference<Link> {
    private String operationRef;
    private String operationId;
    private Map<String, String> parameters;
    private Object requestBody;
    private String description;
    private Server server;
    private Map<String, Object> extensions;
    private String ref;

    @Override
    public Link createInstance() {
        return new Link();
    }
}


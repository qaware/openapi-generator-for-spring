package de.qaware.openapigeneratorforspring.model.server;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * Server
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#serverObject"
 */
@Data
public class Server implements HasExtensions {
    private String url;
    private String description;
    private ServerVariables variables;
    private Map<String, Object> extensions;
}

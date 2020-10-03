package de.qaware.openapigeneratorforspring.model.security;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * SecurityScheme
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#securitySchemeObject"
 */
@Data
public class SecurityScheme implements HasExtensions, HasReference<SecurityScheme> {
    private String type;
    private String description;
    private String name;
    private String ref;
    private String in;
    private String scheme;
    private String bearerFormat;
    private OAuthFlows flows;
    private String openIdConnectUrl;
    private Map<String, Object> extensions;

    @Override
    public SecurityScheme createInstance() {
        return new SecurityScheme();
    }
}

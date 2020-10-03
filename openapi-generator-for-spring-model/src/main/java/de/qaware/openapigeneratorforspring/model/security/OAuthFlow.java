package de.qaware.openapigeneratorforspring.model.security;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * OAuthFlow
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#oauthFlowsObject"
 */
@Data
public class OAuthFlow implements HasExtensions {
    private String authorizationUrl;
    private String tokenUrl;
    private String refreshUrl;
    private Scopes scopes;
    private Map<String, Object> extensions;
}


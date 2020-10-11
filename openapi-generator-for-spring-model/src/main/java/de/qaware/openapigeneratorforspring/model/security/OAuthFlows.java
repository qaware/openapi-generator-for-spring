package de.qaware.openapigeneratorforspring.model.security;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.Data;

import java.util.Map;

/**
 * OAuthFlows
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#oauthFlowsObject"
 */
@Data
public class OAuthFlows implements HasExtensions, HasIsEmpty<OAuthFlows> {
    private OAuthFlow implicit;
    private OAuthFlow password;
    private OAuthFlow clientCredentials;
    private OAuthFlow authorizationCode;
    private Map<String, Object> extensions;

    @Override
    public OAuthFlows createInstance() {
        return new OAuthFlows();
    }
}


package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.OAuthFlow;
import de.qaware.openapigeneratorforspring.model.security.Scopes;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultOAuthFlowAnnotationMapper implements OAuthFlowAnnotationMapper {

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public OAuthFlow map(io.swagger.v3.oas.annotations.security.OAuthFlow annotation) {
        OAuthFlow oAuthFlow = new OAuthFlow();
        setStringIfNotBlank(annotation.authorizationUrl(), oAuthFlow::setAuthorizationUrl);
        setStringIfNotBlank(annotation.tokenUrl(), oAuthFlow::setTokenUrl);
        setStringIfNotBlank(annotation.refreshUrl(), oAuthFlow::setRefreshUrl);
        setMapIfNotEmpty(
                buildStringMapFromStream(Arrays.stream(annotation.scopes()), OAuthScope::name, OAuthScope::description, Scopes::new),
                oAuthFlow::setScopes
        );
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), oAuthFlow::setExtensions);
        return oAuthFlow;
    }
}

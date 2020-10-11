package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.OAuthFlows;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOAuthFlowsAnnotationMapper implements OAuthFlowsAnnotationMapper {

    private final OAuthFlowAnnotationMapper oAuthFlowAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public OAuthFlows map(io.swagger.v3.oas.annotations.security.OAuthFlows annotation) {
        OAuthFlows oAuthFlows = new OAuthFlows();
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.implicit()), oAuthFlows::setImplicit);
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.password()), oAuthFlows::setPassword);
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.clientCredentials()), oAuthFlows::setClientCredentials);
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.authorizationCode()), oAuthFlows::setAuthorizationCode);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), oAuthFlows::setExtensions);
        return oAuthFlows;
    }
}

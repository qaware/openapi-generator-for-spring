package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIf;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultSecuritySchemeAnnotationMapper implements SecuritySchemeAnnotationMapper {

    private final OAuthFlowsAnnotationMapper oAuthFlowsAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public SecurityScheme map(io.swagger.v3.oas.annotations.security.SecurityScheme annotation) {
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(annotation.type().toString());
        setStringIfNotBlank(annotation.description(), securityScheme::setDescription);
        setStringIfNotBlank(annotation.paramName(), securityScheme::setName); // name from annotation is used for components map!
        setIf(annotation.in(), in -> in != SecuritySchemeIn.DEFAULT, in -> securityScheme.setIn(in.toString()));
        setStringIfNotBlank(annotation.scheme(), securityScheme::setScheme);
        setStringIfNotBlank(annotation.bearerFormat(), securityScheme::setBearerFormat);
        setIfNotEmpty(oAuthFlowsAnnotationMapper.map(annotation.flows()), securityScheme::setFlows);
        setStringIfNotBlank(annotation.openIdConnectUrl(), securityScheme::setOpenIdConnectUrl);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), securityScheme::setExtensions);
        return securityScheme;
    }
}

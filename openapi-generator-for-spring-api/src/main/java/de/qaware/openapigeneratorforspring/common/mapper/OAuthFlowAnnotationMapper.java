package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.OAuthFlow;

@FunctionalInterface
public interface OAuthFlowAnnotationMapper {
    OAuthFlow map(io.swagger.v3.oas.annotations.security.OAuthFlow oAuthFlowsAnnotation);
}

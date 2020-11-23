package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.OAuthFlows;

@FunctionalInterface
public interface OAuthFlowsAnnotationMapper {
    OAuthFlows map(io.swagger.v3.oas.annotations.security.OAuthFlows oAuthFlowsAnnotation);
}

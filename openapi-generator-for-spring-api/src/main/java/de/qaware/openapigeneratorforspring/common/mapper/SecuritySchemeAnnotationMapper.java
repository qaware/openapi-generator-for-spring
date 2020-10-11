package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;


public interface SecuritySchemeAnnotationMapper {
    SecurityScheme map(io.swagger.v3.oas.annotations.security.SecurityScheme securitySchemeAnnotation);
}

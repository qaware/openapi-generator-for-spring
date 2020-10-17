package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;


public interface SecurityRequirementAnnotationMapper {
    default SecurityRequirement map(io.swagger.v3.oas.annotations.security.SecurityRequirement securityRequirementAnnotation) {
        return mapArray(securityRequirementAnnotation);
    }

    SecurityRequirement mapArray(io.swagger.v3.oas.annotations.security.SecurityRequirement... securityRequirementAnnotations);
}

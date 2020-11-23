package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;


@FunctionalInterface
public interface SecurityRequirementAnnotationMapper {
    SecurityRequirement mapArray(io.swagger.v3.oas.annotations.security.SecurityRequirement... securityRequirementAnnotations);
}

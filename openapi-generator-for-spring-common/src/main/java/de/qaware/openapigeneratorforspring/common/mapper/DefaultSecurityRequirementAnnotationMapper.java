package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;

import java.util.Arrays;

public class DefaultSecurityRequirementAnnotationMapper implements SecurityRequirementAnnotationMapper {
    @Override
    public SecurityRequirement mapArray(io.swagger.v3.oas.annotations.security.SecurityRequirement... annotations) {
        return OpenApiMapUtils.buildStringMapFromStream(
                Arrays.stream(annotations),
                io.swagger.v3.oas.annotations.security.SecurityRequirement::name,
                annotation -> Arrays.asList(annotation.scopes()),
                SecurityRequirement::new
        );
    }
}

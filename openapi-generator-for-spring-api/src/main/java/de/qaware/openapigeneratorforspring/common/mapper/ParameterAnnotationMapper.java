package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

public interface ParameterAnnotationMapper {
    Parameter buildFromAnnotation(io.swagger.v3.oas.annotations.Parameter parameterAnnotation, MapperContext mapperContext);

    void applyFromAnnotation(Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterAnnotation, MapperContext mapperContext);
}

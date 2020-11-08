package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;

public interface RequestBodyAnnotationMapper {
    RequestBody buildFromAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation, MapperContext mapperContext);

    void applyFromAnnotation(RequestBody requestBody, io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation, MapperContext mapperContext);
}

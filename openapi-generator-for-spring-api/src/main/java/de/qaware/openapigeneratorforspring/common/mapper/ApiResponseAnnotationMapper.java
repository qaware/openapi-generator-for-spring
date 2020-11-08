package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

public interface ApiResponseAnnotationMapper {
    ApiResponse buildFromAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotation, MapperContext mapperContext);

    void applyFromAnnotation(ApiResponse apiResponse, io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotation, MapperContext mapperContext);
}

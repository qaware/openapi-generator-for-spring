package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.List;

public interface ApiResponseAnnotationMapper {
    ApiResponses buildApiResponsesFromAnnotations(List<ApiResponse> apiResponseAnnotations, OperationBuilderContext operationBuilderContext);
}

package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

public interface ApiResponseAnnotationMapper {
    ApiResponses buildApiResponsesFromAnnotations(List<ApiResponse> apiResponseAnnotations, OperationBuilderContext operationBuilderContext);
}

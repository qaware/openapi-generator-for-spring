package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.reflect.Method;

public interface ApiResponseCodeMapper {
    String map(ApiResponse apiResponseAnnotation, OperationBuilderContext operationBuilderContext);

    String getResponseCodeFromMethod(Method method);
}

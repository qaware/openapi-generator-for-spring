package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiAnnotationUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;

public class DefaultApiResponseCodeMapper implements ApiResponseCodeMapper {
    public static final String ANY_RESPONSE_CODE = "*";
    private static final String DEFAULT_RESPONSE_CODE = MergedAnnotation.of(ApiResponse.class)
            .getDefaultValue("responseCode", String.class)
            .orElseThrow(() -> new IllegalStateException("Cannot infer default response code from ApiResponse annotation"));

    @Override
    public String map(ApiResponse apiResponseAnnotation, OperationBuilderContext operationBuilderContext) {
        String annotationResponseCode = apiResponseAnnotation.responseCode();
        if (StringUtils.isNotBlank(annotationResponseCode) && !annotationResponseCode.equals(DEFAULT_RESPONSE_CODE)) {
            if (annotationResponseCode.equals(ANY_RESPONSE_CODE)) {
                return DEFAULT_RESPONSE_CODE;
            }
            return annotationResponseCode;
        }
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        return getResponseCodeFromMethod(method);
    }

    public String getResponseCodeFromMethod(Method method) {
        ResponseStatus responseStatus = OpenApiAnnotationUtils.findAnnotationOnMethodOrClass(method, ResponseStatus.class);
        if (responseStatus != null) {
            return Integer.toString(responseStatus.code().value());
        }
        return Integer.toString(HttpStatus.OK.value());
    }
}

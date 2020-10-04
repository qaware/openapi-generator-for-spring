package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class DefaultApiResponseCodeMapper implements ApiResponseCodeMapper {

    public static final String ANY_RESPONSE_CODE = "*";
    private static final String DEFAULT_RESPONSE_CODE = MergedAnnotation.of(ApiResponse.class)
            .getDefaultValue("responseCode", String.class)
            .orElseThrow(() -> new IllegalStateException("Cannot infer default response code from ApiResponse annotation"));

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public String map(ApiResponse apiResponseAnnotation, OperationBuilderContext operationBuilderContext) {
        String annotationResponseCode = apiResponseAnnotation.responseCode();
        if (StringUtils.isNotBlank(annotationResponseCode) && !annotationResponseCode.equals(DEFAULT_RESPONSE_CODE)) {
            if (annotationResponseCode.equals(ANY_RESPONSE_CODE)) {
                return DEFAULT_RESPONSE_CODE;
            }
            return annotationResponseCode;
        }
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        return getResponseCodeFromMethod(method);
    }

    @Override
    public String getResponseCodeFromMethod(Method method) {
        ResponseStatus responseStatus = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method)
                .findFirstAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return Integer.toString(responseStatus.code().value());
        }
        return Integer.toString(HttpStatus.OK.value());
    }
}

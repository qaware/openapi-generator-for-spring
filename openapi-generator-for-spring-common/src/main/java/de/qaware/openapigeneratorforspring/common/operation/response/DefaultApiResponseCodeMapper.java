package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class DefaultApiResponseCodeMapper implements ApiResponseCodeMapper {

    private static final String DEFAULT_METHOD_RESPONSE_CODE = Integer.toString(HttpStatus.OK.value());
    public static final String DEFAULT_ANNOTATION_RESPONSE_CODE = MergedAnnotation.of(ApiResponse.class)
            .getDefaultValue("responseCode", String.class)
            .orElseThrow(() -> new IllegalStateException("Cannot infer default response code from ApiResponse annotation"));

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public String getFromMethod(Method method) {
        ResponseStatus responseStatus = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method)
                .findFirstAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return Integer.toString(responseStatus.code().value());
        }
        return DEFAULT_METHOD_RESPONSE_CODE;
    }
}

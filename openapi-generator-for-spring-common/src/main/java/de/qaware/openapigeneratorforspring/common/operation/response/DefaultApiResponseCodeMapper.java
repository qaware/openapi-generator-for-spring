package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
public class DefaultApiResponseCodeMapper implements ApiResponseCodeMapper {

    private static final String DEFAULT_METHOD_RESPONSE_CODE = Integer.toString(HttpStatus.OK.value());
    public static final String DEFAULT_ANNOTATION_RESPONSE_CODE = MergedAnnotation.of(ApiResponse.class)
            .getDefaultValue("responseCode", String.class)
            .orElseThrow(() -> new IllegalStateException("Cannot infer default response code from ApiResponse annotation"));

    @Override
    public String map(HandlerMethod handlerMethod) {
        ResponseStatus responseStatus = handlerMethod.getAnnotationsSupplier().findFirstAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return Integer.toString(responseStatus.code().value());
        }
        return DEFAULT_METHOD_RESPONSE_CODE;
    }
}

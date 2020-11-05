package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import static de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesFromMethodCustomizer.DEFAULT_ANNOTATION_RESPONSE_CODE;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@RequiredArgsConstructor
@Order(LOWEST_PRECEDENCE)
public class DefaultApiResponseCodeMapper implements HandlerMethod.ApiResponseCodeMapper {
    @Override
    public String map(HandlerMethod handlerMethod) {
        return DEFAULT_ANNOTATION_RESPONSE_CODE;
    }
}

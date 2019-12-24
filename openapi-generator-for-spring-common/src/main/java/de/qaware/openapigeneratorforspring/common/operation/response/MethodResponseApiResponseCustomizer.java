package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class MethodResponseApiResponseCustomizer implements OperationApiResponseCustomizer, Ordered {

    public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    private final DefaultApiResponseCodeMapper defaultApiResponseCodeMapper;

    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        String responseCodeFromMethod = defaultApiResponseCodeMapper.getResponseCodeFromMethod(method);
        ApiResponse defaultApiResponse = apiResponses.computeIfAbsent(responseCodeFromMethod, ignored -> new ApiResponse());
        if (StringUtils.isBlank(defaultApiResponse.getDescription())) {
            // TODO make this description customizable?
            defaultApiResponse.setDescription("Default response");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

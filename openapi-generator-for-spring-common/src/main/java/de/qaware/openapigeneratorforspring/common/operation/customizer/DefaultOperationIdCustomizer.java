package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;

@RequiredArgsConstructor
public class DefaultOperationIdCustomizer implements OperationCustomizer {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    private final OperationIdProvider operationIdProvider;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        operation.setOperationId(operationIdProvider.getOperationId(operationBuilderContext));
    }

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext,
                          io.swagger.v3.oas.annotations.Operation operationAnnotation) {


        if (StringUtils.isNotBlank(operationAnnotation.operationId())) {
            operation.setOperationId(operationAnnotation.operationId());
        } else {
            customize(operation, operationBuilderContext);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

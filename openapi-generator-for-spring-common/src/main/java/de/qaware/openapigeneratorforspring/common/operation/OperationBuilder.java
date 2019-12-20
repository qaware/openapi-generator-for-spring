package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationIdProvider operationIdProvider;

    public Operation buildOperation(OperationBuilderContext context) {
        return new Operation()
                .operationId(operationIdProvider.getOperationId(context))
                .description(context.getHandlerMethod().getShortLogMessage());
    }
}

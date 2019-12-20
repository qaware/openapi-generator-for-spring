package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationIdProvider operationIdProvider;
    private final List<OperationCustomizer> operationCustomizers;

    public Operation buildOperation(OperationBuilderContext context) {
        Operation operation = new Operation()
                .operationId(operationIdProvider.getOperationId(context));
        for (OperationCustomizer operationCustomizer : operationCustomizers) {
            operationCustomizer.customize(operation, context);
        }
        return operation;
    }
}

package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationIdCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final OperationIdProvider operationIdProvider;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        operation.setOperationId(operationIdProvider.getOperationId(operationBuilderContext.getOperationInfo()));
    }

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        if (operation.getOperationId() == null) {
            customize(operation, operationBuilderContext);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

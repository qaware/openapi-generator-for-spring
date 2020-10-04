package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationAnnotationCustomizer implements OperationCustomizer {
    public static final int ORDER = DEFAULT_ORDER - 10; // run earlier than other customizers

    private final OperationAnnotationMapper operationAnnotationMapper;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext, io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        operationAnnotationMapper.applyFromAnnotation(operation, operationAnnotation,
                operationBuilderContext.getReferencedItemConsumerSupplier(),
                operationBuilderContext.getTagsConsumer()
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

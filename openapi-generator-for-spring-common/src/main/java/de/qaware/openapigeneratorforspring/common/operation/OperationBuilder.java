package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationAnnotationMapper operationAnnotationMapper;
    private final List<OperationCustomizer> operationCustomizers;

    public Operation buildOperation(OperationBuilderContext context) {
        try {
            return getOperationInternal(context);
        } catch (Exception e) {
            // wrap it into exception to help debugging failed open api builds
            throw new OperationBuilderException("Exception encountered while building operation with " + context.getOperationInfo(), e);
        }
    }

    public Operation getOperationInternal(OperationBuilderContext context) {
        io.swagger.v3.oas.annotations.Operation operationAnnotation = context.getOperationInfo().getHandlerMethod().getAnnotationsSupplier()
                .findFirstAnnotation(io.swagger.v3.oas.annotations.Operation.class);
        Operation operation = Optional.ofNullable(operationAnnotation)
                .map(annotation -> operationAnnotationMapper.map(annotation, context.getReferencedItemConsumerSupplier()))
                .orElseGet(Operation::new);
        operationCustomizers.forEach(customizer -> customizer.customize(operation, operationAnnotation, context));
        return operation;
    }
}

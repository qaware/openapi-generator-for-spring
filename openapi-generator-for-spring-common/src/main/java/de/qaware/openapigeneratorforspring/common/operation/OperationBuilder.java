package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationAnnotationMapper operationAnnotationMapper;
    private final List<OperationCustomizer> operationCustomizers;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public Operation buildOperation(OperationBuilderContext context) {
        try {
            return getOperationInternal(context);
        } catch (Exception e) {
            throw new RuntimeException("Exception encountered while building operation with " + context.getOperationInfo(), e);
        }
    }

    public Operation getOperationInternal(OperationBuilderContext context) {
        Method method = context.getOperationInfo().getHandlerMethod().getMethod();

        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(method);
        io.swagger.v3.oas.annotations.Operation operationAnnotation
                = annotationsSupplier.findFirstAnnotation(io.swagger.v3.oas.annotations.Operation.class);

        Operation operation = Optional.ofNullable(operationAnnotation)
                .map(annotation -> operationAnnotationMapper.map(annotation, context.getReferencedItemConsumerSupplier()))
                .orElseGet(Operation::new);

        for (OperationCustomizer operationCustomizer : operationCustomizers) {
            if (operationAnnotation != null) {
                operationCustomizer.customizeWithAnnotationPresent(operation, context, operationAnnotation);
            } else {
                operationCustomizer.customize(operation, context);
            }
        }
        return operation;
    }
}

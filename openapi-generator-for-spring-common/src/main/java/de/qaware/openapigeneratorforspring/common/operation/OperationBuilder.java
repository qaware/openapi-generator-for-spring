package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;

@RequiredArgsConstructor
public class OperationBuilder {

    private final List<OperationCustomizer> operationCustomizers;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public Operation buildOperation(OperationBuilderContext context) {
        Method method = context.getOperationInfo().getHandlerMethod().getMethod();

        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(method);
        io.swagger.v3.oas.annotations.Operation operationAnnotation
                = annotationsSupplier.findFirstAnnotation(io.swagger.v3.oas.annotations.Operation.class);

        Operation operation = new Operation();
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

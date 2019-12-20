package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.List;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationIdProvider operationIdProvider;
    private final List<OperationCustomizer> operationCustomizers;

    public Operation buildOperation(OperationBuilderContext context) {
        Method method = context.getHandlerMethod().getMethod();
        io.swagger.v3.oas.annotations.Operation operationAnnotation
                = AnnotationUtils.findAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);

        Operation operation = new Operation();
        for (OperationCustomizer operationCustomizer : operationCustomizers) {
            if (operationAnnotation != null) {
                operationCustomizer.customize(operation, context, operationAnnotation);
            } else {
                operationCustomizer.customize(operation, context);
            }
        }
        return operation;
    }
}

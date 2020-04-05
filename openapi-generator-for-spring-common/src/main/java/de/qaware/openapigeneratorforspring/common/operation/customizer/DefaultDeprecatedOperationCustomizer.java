package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class DefaultDeprecatedOperationCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        Deprecated deprecatedOnMethodOrClass = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method)
                .findFirstAnnotation(Deprecated.class);
        if (deprecatedOnMethodOrClass != null) {
            operation.deprecated(true);
        }
    }

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        if (operationAnnotation.deprecated()) {
            operation.setDeprecated(true);
        } else {
            customize(operation, operationBuilderContext);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

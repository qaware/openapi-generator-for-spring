package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class DefaultDeprecatedOperationCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        if (operation.getDeprecated() != null) {
            return;
        }
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        Deprecated deprecatedOnMethodOrClass = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method)
                .findFirstAnnotation(Deprecated.class);
        if (deprecatedOnMethodOrClass != null) {
            operation.setDeprecated(true);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

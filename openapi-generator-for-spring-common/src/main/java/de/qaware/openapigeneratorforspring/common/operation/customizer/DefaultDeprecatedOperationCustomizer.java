package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

public class DefaultDeprecatedOperationCustomizer implements OperationCustomizer, Ordered {

    // by default, run after the operation is customized via the annotation
    public static final int ORDER = DefaultOperationAnnotationCustomizer.ORDER + 1;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        Deprecated deprecatedOnMethod = AnnotationUtils.findAnnotation(method, Deprecated.class);
        if (deprecatedOnMethod != null) {
            operation.deprecated(true);
            return;
        }
        Deprecated deprecatedOnClass = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Deprecated.class);
        if (deprecatedOnClass != null) {
            operation.deprecated(true);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

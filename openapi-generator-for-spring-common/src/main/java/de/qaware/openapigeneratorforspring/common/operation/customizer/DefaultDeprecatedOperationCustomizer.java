package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiAnnotationUtils;
import io.swagger.v3.oas.models.Operation;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

public class DefaultDeprecatedOperationCustomizer implements OperationCustomizer, Ordered {

    // by default, run after the operation is customized via the annotation
    public static final int ORDER = DefaultOperationAnnotationCustomizer.ORDER + 1;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        Deprecated deprecatedOnMethodOrClass = OpenApiAnnotationUtils.findAnnotationOnMethodOrClass(method, Deprecated.class);
        if (deprecatedOnMethodOrClass != null) {
            operation.deprecated(true);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

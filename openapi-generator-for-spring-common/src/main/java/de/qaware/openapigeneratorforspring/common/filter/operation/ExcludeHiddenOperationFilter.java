package de.qaware.openapigeneratorforspring.common.filter.operation;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

public class ExcludeHiddenOperationFilter implements OperationFilter {
    @Override
    public boolean accept(io.swagger.v3.oas.models.Operation operation, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        Hidden hiddenOnMethod = AnnotationUtils.findAnnotation(method, Hidden.class);
        if (hiddenOnMethod != null) {
            return false;
        }
        Hidden hiddenOnClass = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Hidden.class);
        if (hiddenOnClass != null) {
            return false;
        }
        Operation operationAnnotation = AnnotationUtils.findAnnotation(method, Operation.class);
        return operationAnnotation == null || !operationAnnotation.hidden();
    }
}

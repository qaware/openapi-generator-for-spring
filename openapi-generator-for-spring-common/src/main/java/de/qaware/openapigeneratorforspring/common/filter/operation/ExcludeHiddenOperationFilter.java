package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ExcludeHiddenOperationFilter implements OperationFilter {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public boolean accept(io.swagger.v3.oas.models.Operation operation, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        Hidden hiddenOnMethodOrClass = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method)
                .findFirstAnnotation(Hidden.class);
        if (hiddenOnMethodOrClass != null) {
            return false;
        }
        Operation operationAnnotation = AnnotationUtils.findAnnotation(method, Operation.class);
        return operationAnnotation == null || !operationAnnotation.hidden();
    }
}

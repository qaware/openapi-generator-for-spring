package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ExcludeHiddenHandlerMethodFilter implements HandlerMethodFilter {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public boolean accept(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        Hidden hiddenOnMethodOrClass = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method)
                .findFirstAnnotation(Hidden.class);
        if (hiddenOnMethodOrClass != null) {
            return false;
        }
        Operation operationAnnotation = annotationsSupplierFactory.createFromAnnotatedElement(method)
                .findFirstAnnotation(Operation.class);
        return operationAnnotation == null || !operationAnnotation.hidden();
    }
}

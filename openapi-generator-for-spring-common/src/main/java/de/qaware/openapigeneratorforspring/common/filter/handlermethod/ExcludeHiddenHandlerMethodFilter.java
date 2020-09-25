package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ExcludeHiddenHandlerMethodFilter implements HandlerMethodFilter {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public boolean accept(HandlerMethodWithInfo handlerMethodWithInfo) {
        Method method = handlerMethodWithInfo.getHandlerMethod().getMethod();
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

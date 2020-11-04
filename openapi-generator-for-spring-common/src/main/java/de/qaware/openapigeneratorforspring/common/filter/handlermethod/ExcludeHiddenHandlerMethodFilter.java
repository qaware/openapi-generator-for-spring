package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

public class ExcludeHiddenHandlerMethodFilter implements HandlerMethodFilter {
    @Override
    public boolean accept(HandlerMethodWithInfo handlerMethodWithInfo) {
        AnnotationsSupplier annotationsSupplier = handlerMethodWithInfo.getHandlerMethod().getAnnotationsSupplier();
        Hidden hiddenOnMethodOrClass = annotationsSupplier.findFirstAnnotation(Hidden.class);
        if (hiddenOnMethodOrClass != null) {
            return false;
        }
        Operation operationAnnotation = annotationsSupplier.findFirstAnnotation(Operation.class);
        return operationAnnotation == null || !operationAnnotation.hidden();
    }
}

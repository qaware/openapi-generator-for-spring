package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

public class ExcludeHiddenHandlerMethodFilter implements HandlerMethodFilter {
    @Override
    public boolean accept(HandlerMethodWithInfo handlerMethodWithInfo) {
        HandlerMethod handlerMethod = handlerMethodWithInfo.getHandlerMethod();
        boolean hiddenOnHandlerMethod = handlerMethod
                .findAnnotations(Hidden.class)
                .findAny().isPresent();
        return !hiddenOnHandlerMethod && handlerMethod.findAnnotations(Operation.class).noneMatch(Operation::hidden);
    }
}

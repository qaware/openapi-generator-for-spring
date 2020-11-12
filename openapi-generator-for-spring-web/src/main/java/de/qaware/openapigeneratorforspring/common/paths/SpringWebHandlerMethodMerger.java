package de.qaware.openapigeneratorforspring.common.paths;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpringWebHandlerMethodMerger implements HandlerMethod.Merger {
    @Nullable
    @Override
    public HandlerMethod merge(List<HandlerMethod> handlerMethods) {
        List<SpringWebHandlerMethod> springWebHandlerMethods = handlerMethods.stream()
                .map(handlerMethod -> {
                    if (handlerMethod instanceof SpringWebHandlerMethod) {
                        return (SpringWebHandlerMethod) handlerMethod;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (springWebHandlerMethods.isEmpty()) {
            return null;
        }
        if (springWebHandlerMethods.size() != handlerMethods.size()) {
            throw new IllegalStateException("Cannot map handler methods not purely of type " + SpringWebHandlerMethod.class.getSimpleName());
        }
        // TODO implement
        return null;
    }
}

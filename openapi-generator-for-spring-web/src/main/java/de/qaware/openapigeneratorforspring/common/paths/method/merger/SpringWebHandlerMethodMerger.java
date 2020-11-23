package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpringWebHandlerMethodMerger implements HandlerMethod.Merger {

    private final SpringWebHandlerMethodParameterMerger springWebHandlerMethodParameterMerger;
    private final SpringWebHandlerMethodIdentifierMerger springWebHandlerMethodIdentifierMerger;
    private final SpringWebHandlerMethodRequestBodyMerger springWebHandlerMethodRequestBodyMerger;
    private final SpringWebHandlerMethodResponseMerger springWebHandlerMethodResponseMerger;

    @Nullable
    @Override
    public HandlerMethod merge(List<HandlerMethod> handlerMethods) {
        return findSpringWebHandlerMethods(handlerMethods)
                .map(springWebHandlerMethods -> new MergedSpringWebHandlerMethod(
                        AnnotationsSupplier.merge(handlerMethods.stream()),
                        springWebHandlerMethodParameterMerger.mergeParameters(handlerMethods),
                        springWebHandlerMethodIdentifierMerger.mergeIdentifiers(handlerMethods),
                        // until here, merging is independent of springWebHandlerMethods
                        // but eventually providing merged request bodies and merged responses
                        // requires assumptions about how Spring Web handler methods work
                        springWebHandlerMethods,
                        springWebHandlerMethodRequestBodyMerger.mergeRequestBodies(springWebHandlerMethods),
                        springWebHandlerMethodResponseMerger.mergeResponses(springWebHandlerMethods)
                ))
                .orElse(null);
    }

    private Optional<List<SpringWebHandlerMethod>> findSpringWebHandlerMethods(List<HandlerMethod> handlerMethods) {
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
            return Optional.empty();
        }
        if (springWebHandlerMethods.size() != handlerMethods.size()) {
            throw new IllegalStateException("Cannot map handler methods not purely of type " + SpringWebHandlerMethod.class.getSimpleName());
        }
        return Optional.of(springWebHandlerMethods);
    }
}

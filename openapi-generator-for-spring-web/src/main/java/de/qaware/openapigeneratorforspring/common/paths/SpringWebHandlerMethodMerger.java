package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
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

        String mergedIdentifier = handlerMethods.stream()
                .map(HandlerMethod::getIdentifier)
                .distinct()
                .collect(Collectors.joining("_"));

        AnnotationsSupplier mergedAnnotationSupplier = handlerMethods.stream()
                .map(HandlerMethod::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);

        // group parameters by name
        List<HandlerMethod.Parameter> mergedParameters = handlerMethods.stream()
                .map(HandlerMethod::getParameters)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(HandlerMethod.Parameter::getName, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> entry.getKey()
                        .map(parameterName -> mergeParameters(parameterName, entry.getValue()))
                        .orElseThrow(() -> new IllegalStateException("Cannot merge handler methods with unnamed parameters: " + entry.getValue()))
                )
                .collect(Collectors.toList());

        return new MergedHandlerMethod(mergedIdentifier, mergedAnnotationSupplier, mergedParameters);
    }

    @RequiredArgsConstructor
    @Getter
    private static class MergedHandlerMethod implements HandlerMethod {
        private final String identifier;
        private final AnnotationsSupplier annotationsSupplier;
        private final List<HandlerMethod.Parameter> parameters;
    }


    static AbstractSpringWebHandlerMethod.SpringWebParameter mergeParameters(String parameterName, List<HandlerMethod.Parameter> parameters) {
        AnnotationsSupplier mergedAnnotationsSupplier = parameters.stream()
                .map(HandlerMethod.Parameter::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);
        HandlerMethod.Type mergedType = parameters.stream()
                .flatMap(p -> p.getType().map(Stream::of).orElseGet(Stream::empty))
                .reduce((a, b) -> AbstractSpringWebHandlerMethod.SpringWebType.of(chooseType(a, b), a.getAnnotationsSupplier().andThen(b.getAnnotationsSupplier())))
                .orElseGet(() -> AbstractSpringWebHandlerMethod.SpringWebType.of(Void.class, AnnotationsSupplier.EMPTY));
        return AbstractSpringWebHandlerMethod.SpringWebParameter.of(parameterName, mergedType, mergedAnnotationsSupplier);
    }

    private static Type chooseType(HandlerMethod.Type a, HandlerMethod.Type b) {
        if (a.getType().equals(b.getType())) {
            return a.getType();
        }
        LOGGER.warn("Cannot handle conflicting parameter type {} vs. {}, assuming Void", a.getType(), b.getType());
        return Void.class;
    }

}

package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.AbstractSpringWebHandlerMethod.SpringWebParameter;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        String mergedIdentifier = mergeIdentifiers(handlerMethods);

        AnnotationsSupplier mergedAnnotationSupplier = handlerMethods.stream()
                .map(HandlerMethod::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);

        // group parameters by name after merging them
        Map<String, SpringWebParameter> mergedParameters = OpenApiMapUtils.buildStringMapFromStream(
                handlerMethods.stream()
                        .map(HandlerMethod::getParameters)
                        .flatMap(Collection::stream)
                        .collect(Collectors.groupingBy(HandlerMethod.Parameter::getName, LinkedHashMap::new, Collectors.toList()))
                        .entrySet().stream()
                        .map(entry -> entry.getKey()
                                .map(parameterName -> Pair.of(parameterName, mergeParameters(parameterName, entry.getValue())))
                                .orElseThrow(() -> new IllegalStateException("Cannot merge handler methods with unnamed parameters: " + entry.getValue()))
                        ),
                Pair::getKey,
                Pair::getValue
        );

        // until here, merging is independent of springWebHandlerMethods
        // but eventually providing merged request bodies and merged responses
        // requires assumptions about how Spring Web handler methods work
        return new MergedSpringWebHandlerMethod(mergedIdentifier, mergedAnnotationSupplier, springWebHandlerMethods, mergedParameters);
    }

    private static String mergeIdentifiers(List<HandlerMethod> handlerMethods) {
        LinkedList<String> identifiers = handlerMethods.stream()
                .map(HandlerMethod::getIdentifier)
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
        String commonPrefix = StringUtils.getCommonPrefix(identifiers.getFirst(), identifiers.getLast());
        return Stream.concat(
                Stream.of(commonPrefix),
                identifiers.stream()
                        .map(identifier -> identifier.substring(commonPrefix.length()))
                        .filter(s -> !s.isEmpty())
        ).collect(Collectors.joining());
    }

    private static SpringWebParameter mergeParameters(String parameterName, List<HandlerMethod.Parameter> parameters) {
        AnnotationsSupplier mergedAnnotationsSupplier = parameters.stream()
                .map(HandlerMethod.Parameter::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);
        HandlerMethod.Type mergedType = parameters.stream()
                .flatMap(parameter -> parameter.getType()
                        .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                )
                .reduce((a, b) -> AbstractSpringWebHandlerMethod.SpringWebType.of(chooseParameterType(a, b), a.getAnnotationsSupplier().andThen(b.getAnnotationsSupplier())))
                .orElseThrow(() -> new IllegalStateException("Grouped parameters should contain at least one entry"));
        return SpringWebParameter.of(parameterName, mergedType, mergedAnnotationsSupplier);
    }

    private static Type chooseParameterType(HandlerMethod.Type a, HandlerMethod.Type b) {
        if (a.getType().equals(b.getType())) {
            return a.getType();
        }
        throw new IllegalStateException("Cannot handle conflicting parameter type " + a + " vs. " + b);
    }
}

package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.AbstractSpringWebHandlerMethod.SpringWebParameter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

        // merge parameters by name
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

        // until here, merging is independent of springWebHandlerMethods
        // but eventually providing merged request bodies and merged responses
        // requires assumptions about how Spring Web handler methods work
        return new MergedSpringWebHandlerMethod(mergedIdentifier, mergedAnnotationSupplier, mergedParameters, springWebHandlerMethods);
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

    private static HandlerMethod.Parameter mergeParameters(String parameterName, List<HandlerMethod.Parameter> parameters) {
        AnnotationsSupplier mergedAnnotationsSupplier = mergeParametersAnnotationSupplier(parameters.stream());
        HandlerMethod.Type mergedType = mergeParametersType(parameters.stream())
                .orElseThrow(() -> new IllegalStateException("Grouped parameters should contain at least one entry"));
        return SpringWebParameter.of(parameterName, mergedType, mergedAnnotationsSupplier);
    }

    static AnnotationsSupplier mergeParametersAnnotationSupplier(Stream<HandlerMethod.Parameter> parameters) {
        return parameters.map(HandlerMethod.Parameter::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);
    }

    static Optional<HandlerMethod.Type> mergeParametersType(Stream<HandlerMethod.Parameter> parameters) {
        return parameters
                .map(HandlerMethod.Parameter::getType)
                .map(type -> type.orElseThrow(() -> new IllegalStateException("Spring Web Handler Method Parameter should always have a type present")))
                .reduce(SpringWebHandlerMethodMerger::mergeType);
    }

    static AbstractSpringWebHandlerMethod.SpringWebType mergeType(HandlerMethod.Type a, HandlerMethod.Type b) {
        if (isNotVoid(a) && isNotVoid(b) && !a.getType().equals(b.getType())) {
            throw new IllegalStateException("Cannot merge conflicting types: " + a + " vs. " + b);
        }
        return AbstractSpringWebHandlerMethod.SpringWebType.of(
                chooseParameterType(a, b),
                a.getAnnotationsSupplier().andThen(b.getAnnotationsSupplier())
        );
    }

    private static Type chooseParameterType(HandlerMethod.Type a, HandlerMethod.Type b) {
        return isNotVoid(a) ? a.getType() : b.getType();
    }

    private static boolean isNotVoid(HandlerMethod.Type type) {
        return !void.class.equals(type.getType()) && !Void.class.equals(type.getType());
    }
}

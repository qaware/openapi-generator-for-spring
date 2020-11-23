package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpringWebHandlerMethodParameterMerger {

    private final SpringWebHandlerMethodTypeMerger springWebHandlerMethodTypeMerger;

    public List<HandlerMethod.Parameter> mergeParameters(List<HandlerMethod> handlerMethods) {
        return handlerMethods.stream()
                .map(HandlerMethod::getParameters)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(HandlerMethod.Parameter::getName, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> entry.getKey()
                        .map(parameterName -> buildMergedParameter(parameterName, entry.getValue()))
                        .orElseThrow(() -> new IllegalStateException("Cannot merge handler methods with unnamed parameters: " + entry.getValue()))
                )
                .collect(Collectors.toList());
    }

    private HandlerMethod.Parameter buildMergedParameter(String parameterName, List<HandlerMethod.Parameter> parameters) {
        AnnotationsSupplier mergedAnnotationsSupplier = AnnotationsSupplier.merge(parameters.stream());
        HandlerMethod.Type mergedType = springWebHandlerMethodTypeMerger.mergeTypes(parameters.stream())
                .orElseThrow(() -> new IllegalStateException("Grouped parameters should contain at least one entry"));
        return new AbstractSpringWebHandlerMethod.SpringWebParameter(parameterName, mergedType, mergedAnnotationsSupplier);
    }
}

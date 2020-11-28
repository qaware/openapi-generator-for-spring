package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod.SpringWebParameter;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpringWebHandlerMethodParameterMerger {

    private final SpringWebHandlerMethodTypeMerger springWebHandlerMethodTypeMerger;

    public List<HandlerMethod.Parameter> mergeParameters(List<SpringWebHandlerMethod> handlerMethods) {
        return handlerMethods.stream()
                .flatMap(handlerMethod -> handlerMethod.getParameters().stream()
                        .map(parameter -> Pair.of(parameter.getName(), Pair.of(parameter, handlerMethod)))
                )
                .collect(OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList())
                .entrySet().stream()
                .map(entry -> entry.getKey()
                        .map(parameterName -> {
                            List<HandlerMethod.Parameter> parameters = entry.getValue().stream()
                                    .map(Pair::getLeft)
                                    .collect(Collectors.toList());
                            List<HandlerMethod> handlerMethodsForParameters = entry.getValue().stream()
                                    .map(Pair::getRight)
                                    .collect(Collectors.toList());
                            return buildMergedParameter(parameterName, parameters, parameter -> {
                                if (!handlerMethodsForParameters.containsAll(handlerMethods)) {
                                    parameter.setRequired(false); // otherwise the UI doesn't work
                                    if (parameter.getDescription() == null) {
                                        parameter.setDescription("Only used by " + handlerMethodsForParameters.stream()
                                                .map(HandlerMethod::getIdentifier)
                                                .collect(Collectors.joining(", "))
                                        );
                                    }
                                }
                            });
                        })
                        .orElseThrow(() -> new IllegalStateException("Cannot merge handler methods with unnamed parameters: " + entry.getValue()))
                )
                .collect(Collectors.toList());
    }

    private HandlerMethod.Parameter buildMergedParameter(String parameterName, List<HandlerMethod.Parameter> parameters, Consumer<Parameter> parameterCustomizer) {
        AnnotationsSupplier mergedAnnotationsSupplier = AnnotationsSupplier.merge(parameters.stream());
        HandlerMethod.Type mergedType = springWebHandlerMethodTypeMerger.mergeTypes(parameters.stream())
                .orElseThrow(() -> new IllegalStateException("Grouped parameters should contain at least one entry"));
        return new SpringWebParameter(parameterName, mergedType, mergedAnnotationsSupplier) {
            @Override
            public void customize(Parameter parameter) {
                parameterCustomizer.accept(parameter);
            }
        };
    }
}

package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethod.RequestBodyParameter;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodMapper.ifEmptyUseSingleAllValue;
import static de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodMerger.mergeParametersAnnotationSupplier;
import static de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodMerger.mergeParametersType;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesTo;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;
import static java.util.Collections.emptyList;

@Getter
class MergedSpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {
    private final String identifier;
    private final List<RequestBody> requestBodies;
    private final List<Response> responses;

    MergedSpringWebHandlerMethod(String identifier, AnnotationsSupplier annotationsSupplier,
                                 List<Parameter> parameters, List<SpringWebHandlerMethod> handlerMethods) {
        super(annotationsSupplier, parameters);
        this.identifier = identifier;
        this.requestBodies = mergeRequestBodies(handlerMethods);
        this.responses = mergeResponses(handlerMethods);
    }

    private static List<Response> mergeResponses(List<SpringWebHandlerMethod> handlerMethods) {
        List<Response> mergedResponses = new ArrayList<>();
        Map<String, Map<Set<String>, List<SpringWebHandlerMethod>>> groupedHandlerMethods = handlerMethods.stream()
                .map(method -> Pair.of(method.getResponseCode(), Pair.of(method.findProducesContentTypes(), method)))
                .collect(groupingByPairKeyAndCollectingValuesTo(groupingByPairKeyAndCollectingValuesToList()));
        groupedHandlerMethods.forEach((responseCode, typesGroupedByProduces) ->
                typesGroupedByProduces.forEach((producesContentTypes, methods) ->
                        mergedResponses.add(
                                new SpringWebResponse(responseCode, ifEmptyUseSingleAllValue(producesContentTypes), mergeReturnTypes(methods)) {
                                    @Override
                                    public void customize(ApiResponse apiResponse) {
                                        Content content = apiResponse.getContent();
                                        if (content != null && content.size() == 1
                                                && groupedHandlerMethods.get(responseCode).size() == 1
                                                && content.values().stream().allMatch(HasIsEmpty::isEmpty)
                                        ) {
                                            apiResponse.setContent(null);
                                        }
                                    }
                                }
                        )
                )
        );
        return mergedResponses;
    }

    private static Optional<HandlerMethod.Type> mergeReturnTypes(List<SpringWebHandlerMethod> handlerMethods) {
        return handlerMethods.stream()
                .map(SpringWebHandlerMethod::getReturnType)
                .reduce(SpringWebHandlerMethodMerger::mergeType)
                .map(x -> x);
    }

    private static List<RequestBody> mergeRequestBodies(List<SpringWebHandlerMethod> handlerMethods) {
        // group request bodies by (unique) list of consumesContentTypes
        // this way, we can detect best how to build the final request body list
        Map<Set<String>, List<Optional<RequestBodyParameter>>> groupedRequestBodyParameters = handlerMethods.stream()
                .map(springWebHandlerMethod -> Pair.of(springWebHandlerMethod.findConsumesContentTypes(), springWebHandlerMethod.findRequestBodyParameter()))
                .collect(groupingByPairKeyAndCollectingValuesToList());

        // ALL_VALUE is collected as an empty list key in the map
        List<Optional<RequestBodyParameter>> allValueRequestBodyParameters = groupedRequestBodyParameters.get(Collections.emptySet());
        if (groupedRequestBodyParameters.size() == 1 && allValueRequestBodyParameters != null && allValueRequestBodyParameters.stream().noneMatch(Optional::isPresent)) {
            // we can omit the request bodies entirely if there's only empty request bodies matching for ALL_VALUE
            return emptyList();
        }

        boolean noEmptyEntries = groupedRequestBodyParameters.values().stream()
                .flatMap(Collection::stream)
                .allMatch(Optional::isPresent);

        return groupedRequestBodyParameters.entrySet().stream()
                .map(entry -> {
                    Set<String> consumesContentTypes = entry.getKey();
                    List<RequestBodyParameter> requestBodyParameters = entry.getValue().stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    return mergeParametersType(requestBodyParameters.stream().map(RequestBodyParameter::getParameter))
                            .map(parameterType -> {
                                boolean required = requestBodyParameters.stream()
                                        .map(RequestBodyParameter::isRequired)
                                        .reduce((a, b) -> a || b) // TODO think about merging the required flag, maybe make it customizable?
                                        .orElse(false);
                                return new SpringWebRequestBody(
                                        mergeParametersAnnotationSupplier(requestBodyParameters.stream().map(RequestBodyParameter::getParameter)),
                                        consumesContentTypes,
                                        Optional.of(parameterType),
                                        required
                                ) {
                                    @Override
                                    public void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
                                        if (!noEmptyEntries) {
                                            return;
                                        }
                                        super.customize(requestBody);
                                    }
                                };
                            })
                            .map(RequestBody.class::cast)
                            .orElseGet(() -> new EmptyRequestBody(ifEmptyUseSingleAllValue(consumesContentTypes)));
                })
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    private static class EmptyRequestBody implements RequestBody {
        @Getter
        private final Set<String> consumesContentTypes;

        @Override
        public Optional<Type> getType() {
            return Optional.empty();
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return AnnotationsSupplier.EMPTY;
        }
    }
}

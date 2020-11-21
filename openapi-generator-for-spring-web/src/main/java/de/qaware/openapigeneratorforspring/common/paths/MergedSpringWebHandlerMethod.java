package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodMapper.ifEmptyUseSingleAllValue;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesTo;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;
import static java.util.Collections.emptyList;

@Getter
class MergedSpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {
    private final String identifier;
    private final List<RequestBody> requestBodies;
    private final List<Response> responses;

    MergedSpringWebHandlerMethod(String identifier, AnnotationsSupplier annotationsSupplier,
                                 List<SpringWebHandlerMethod> handlerMethods,
                                 Map<String, SpringWebParameter> mergedParametersByName) {
        super(annotationsSupplier, new ArrayList<>(mergedParametersByName.values()));
        this.identifier = identifier;
        this.requestBodies = mergeRequestBodies(handlerMethods, mergedParametersByName);
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

    private static Optional<HandlerMethod.Type> mergeReturnTypes(List<SpringWebHandlerMethod> methods) {
        return methods.stream()
                .map(SpringWebHandlerMethod::getReturnType)
                .reduce((a, b) -> {
                    if (isNotVoid(a) && isNotVoid(b) && !a.getType().equals(b.getType())) {
                        throw new IllegalStateException("Cannot merge handler methods with conflicting return types: " + methods);
                    }
                    return (isNotVoid(a) ? a : b).withAnnotationsSupplier(a.getAnnotationsSupplier().andThen(b.getAnnotationsSupplier()));
                })
                .map(x -> x); // cast to upper class
    }

    private static boolean isNotVoid(HandlerMethod.Type type) {
        return !void.class.equals(type.getType()) && !Void.class.equals(type.getType());
    }

    private static List<RequestBody> mergeRequestBodies(List<SpringWebHandlerMethod> springWebHandlerMethods,
                                                        Map<String, SpringWebParameter> mergedParametersByName) {
        // group request bodies by (unique) list of consumesContentTypes
        // this way, we can detect best how to build the final request body list
        Map<Set<String>, List<RequestBody>> mergedRequestBodies = springWebHandlerMethods.stream()
                .flatMap(springWebHandlerMethod -> {
                    Set<String> consumesContentTypes = springWebHandlerMethod.findConsumesContentTypes();
                    List<Pair<Set<String>, RequestBody>> requestBodiesFromMethod = springWebHandlerMethod.getParameters().stream()
                            .map(Parameter::getName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            // important to build the request body based on the merged parameter,
                            // this ensures, for example, that the annotation supplier is the union of all parameters by name
                            .map(mergedParametersByName::get)
                            .flatMap(mergedParameter -> SpringWebHandlerMethod.buildSpringWebRequestBodies(mergedParameter, consumesContentTypes))
                            .map(requestBody -> Pair.of(consumesContentTypes, requestBody))
                            .collect(Collectors.toList());
                    // prevent loosing consumesContentTypes (might be even empty!) if the method doesn't build any request bodies
                    if (requestBodiesFromMethod.isEmpty()) {
                        return Stream.of(buildDummyPair(consumesContentTypes, DummyRequestBody::of));
                    }
                    return requestBodiesFromMethod.stream();
                })
                .collect(groupingByPairKeyAndCollectingValuesToList());

        // ALL_VALUE is collected as an empty list key in the map
        List<RequestBody> allValueRequestBodies = mergedRequestBodies.get(Collections.emptySet());
        if (mergedRequestBodies.size() == 1 && allValueRequestBodies != null
                && allValueRequestBodies.stream().allMatch(x -> x instanceof DummyRequestBody)) {
            // we can omit the content entirely if there's only dummy request bodies matching for ALL_VALUE
            return emptyList();
        }

        return mergedRequestBodies.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static <T> Pair<Set<String>, T> buildDummyPair(Set<String> contentTypes, Function<Set<String>, T> dummyConstructor) {
        // note that the key should stay empty, but the DummyRequestBody must never
        // return a non-empty list to the outside world
        return Pair.of(contentTypes, dummyConstructor.apply(ifEmptyUseSingleAllValue(contentTypes)));
    }

    @RequiredArgsConstructor
    private static class DummyRequestBody implements RequestBody {
        @Getter
        private final Set<String> consumesContentTypes;

        static RequestBody of(Set<String> consumesContentTypes) {
            return new DummyRequestBody(ifEmptyUseSingleAllValue(consumesContentTypes));
        }

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

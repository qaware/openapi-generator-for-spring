package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.AbstractSpringWebHandlerMethod.SpringWebParameter;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

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
                        )
                ,
                Pair::getKey,
                Pair::getValue
        );

        // until here, merging is generic (doesn't depend on springWebHandlerMethods)
        // but actual

        List<HandlerMethod.RequestBody> requestBodies = mergeRequestBodies(springWebHandlerMethods, mergedParameters);

        return new MergedHandlerMethod(mergedIdentifier, mergedAnnotationSupplier, mergedParameters.values(), requestBodies);
    }

    private static List<HandlerMethod.RequestBody> mergeRequestBodies(List<SpringWebHandlerMethod> springWebHandlerMethods, Map<String, SpringWebParameter> mergedParameters) {
        Map<Set<String>, List<HandlerMethod.RequestBody>> mergedRequestBodies = springWebHandlerMethods.stream()
                .flatMap(springWebHandlerMethod -> {
                    Set<String> consumesContentTypes = springWebHandlerMethod.findConsumesContentTypes();
                    List<Pair<Set<String>, HandlerMethod.RequestBody>> requestBodiesFromMethod = springWebHandlerMethod.getParameters().stream()
                            .map(HandlerMethod.Parameter::getName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            // important to build the request body based on the merged parameter,
                            // this ensures, for example, that the annotation supplier is the union of all parameters by name
                            .map(mergedParameters::get)
                            .flatMap(mergedParameter -> SpringWebHandlerMethod.buildSpringWebRequestBodies(mergedParameter, consumesContentTypes))
                            .map(requestBody -> Pair.of(consumesContentTypes, requestBody))
                            .collect(Collectors.toList());
                    // prevent loosing consumesContentTypes (might be even empty!) if the method doesn't build any request bodies
                    if (requestBodiesFromMethod.isEmpty()) {
                        return Stream.of(DummyRequestBody.of(consumesContentTypes));
                    }
                    return requestBodiesFromMethod.stream();
                })
                .collect(groupingByPairKeyAndCollectingValuesToList());

        List<HandlerMethod.RequestBody> allValueRequestBodies = mergedRequestBodies.get(Collections.emptySet());
        if (allValueRequestBodies != null && mergedRequestBodies.size() == 1
                && allValueRequestBodies.stream().allMatch(x -> x instanceof DummyRequestBody)) {
            return emptyList();
        }

        return mergedRequestBodies.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Getter
    private static class MergedHandlerMethod extends AbstractSpringWebHandlerMethod {
        private final String identifier;
        private final List<RequestBody> requestBodies;

        private MergedHandlerMethod(
                String identifier, AnnotationsSupplier annotationsSupplier,
                Collection<SpringWebParameter> parameters, List<RequestBody> requestBodies
        ) {
            super(annotationsSupplier, new ArrayList<>(parameters));
            this.identifier = identifier;
            this.requestBodies = requestBodies;
        }
    }


    static SpringWebParameter mergeParameters(String parameterName, List<HandlerMethod.Parameter> parameters) {
        AnnotationsSupplier mergedAnnotationsSupplier = parameters.stream()
                .map(HandlerMethod.Parameter::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);
        HandlerMethod.Type mergedType = parameters.stream()
                .flatMap(p -> p.getType().map(Stream::of).orElseGet(Stream::empty))
                .reduce((a, b) -> AbstractSpringWebHandlerMethod.SpringWebType.of(chooseType(a, b), a.getAnnotationsSupplier().andThen(b.getAnnotationsSupplier())))
                .orElseGet(() -> AbstractSpringWebHandlerMethod.SpringWebType.of(Void.class, AnnotationsSupplier.EMPTY));
        return SpringWebParameter.of(parameterName, mergedType, mergedAnnotationsSupplier);
    }

    private static Type chooseType(HandlerMethod.Type a, HandlerMethod.Type b) {
        if (a.getType().equals(b.getType())) {
            return a.getType();
        }
        LOGGER.warn("Cannot handle conflicting parameter type {} vs. {}, assuming Void", a.getType(), b.getType());
        return Void.class;
    }

    @RequiredArgsConstructor
    private static class DummyRequestBody implements HandlerMethod.RequestBody {
        private final Set<String> consumesContentTypes;

        static Pair<Set<String>, HandlerMethod.RequestBody> of(Set<String> consumesContentTypes) {
            return Pair.of(consumesContentTypes, new DummyRequestBody(consumesContentTypes));
        }

        @Override
        public Optional<HandlerMethod.Type> getType() {
            return Optional.of(new HandlerMethod.Type() {
                @Override
                public Type getType() {
                    // TODO check if returning an "dummy" type here is actually necessary to get it working in Swagger UI
                    // maybe returning Void.class is enough?
                    return Void.class;
                }

                @Override
                public AnnotationsSupplier getAnnotationsSupplier() {
                    return AnnotationsSupplier.EMPTY;
                }
            });
        }

        @Override
        public Set<String> getConsumesContentTypes() {
            return consumesContentTypes.isEmpty() ? singleton(org.springframework.http.MediaType.ALL_VALUE) : consumesContentTypes;
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return AnnotationsSupplier.EMPTY;
        }
    }
}

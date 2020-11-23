package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterMapper.RequestBodyParameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper.ifEmptyUseSingleAllValue;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class SpringWebHandlerMethodRequestBodyMerger {

    private final SpringWebHandlerMethodTypeMerger typeMerger;
    private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;
    private final SpringWebHandlerMethodRequestBodyParameterMapper requestBodyParameterMapper;

    public List<HandlerMethod.RequestBody> mergeRequestBodies(List<SpringWebHandlerMethod> handlerMethods) {
        // group request bodies by (unique) list of consumesContentTypes
        // this way, we can detect best how to build the final request body list
        Map<Set<String>, List<Optional<RequestBodyParameter>>> groupedRequestBodyParameters = handlerMethods.stream()
                .map(handlerMethod -> Pair.of(contentTypesMapper.findConsumesContentTypes(handlerMethod), requestBodyParameterMapper.findRequestBodyParameter(handlerMethod)))
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
                    return typeMerger.mergeTypes(requestBodyParameters.stream().map(RequestBodyParameter::getParameter))
                            .map(parameterType -> {
                                boolean required = requestBodyParameters.stream()
                                        .map(RequestBodyParameter::isRequired)
                                        .reduce((a, b) -> a || b) // TODO think about merging the required flag, maybe make it customizable?
                                        .orElse(false);
                                return new AbstractSpringWebHandlerMethod.SpringWebRequestBody(
                                        AnnotationsSupplier.merge(requestBodyParameters.stream().map(RequestBodyParameter::getParameter)),
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
                            .map(HandlerMethod.RequestBody.class::cast)
                            .orElseGet(() -> new EmptyRequestBody(ifEmptyUseSingleAllValue(consumesContentTypes)));
                })
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    private static class EmptyRequestBody implements HandlerMethod.RequestBody {
        @Getter
        private final Set<String> consumesContentTypes;

        @Override
        public Optional<HandlerMethod.Type> getType() {
            return Optional.empty();
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return AnnotationsSupplier.EMPTY;
        }
    }

}

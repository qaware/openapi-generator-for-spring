package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodReturnTypeMapper;
import de.qaware.openapigeneratorforspring.model.media.Content;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper.ifEmptyUseSingleAllValue;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesTo;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;

@RequiredArgsConstructor
public class SpringWebHandlerMethodResponseMerger {

    private final SpringWebHandlerMethodTypeMerger typeMerger;
    private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;
    private final SpringWebHandlerMethodResponseCodeMapper responseCodeMapper;
    private final SpringWebHandlerMethodReturnTypeMapper returnTypeMapper;

    public List<HandlerMethod.Response> mergeResponses(List<SpringWebHandlerMethod> handlerMethods) {
        List<HandlerMethod.Response> mergedResponses = new ArrayList<>();
        Map<String, Map<Set<String>, List<SpringWebHandlerMethod>>> groupedHandlerMethods = handlerMethods.stream()
                .map(method -> Pair.of(responseCodeMapper.getResponseCode(method), Pair.of(contentTypesMapper.findProducesContentTypes(method), method)))
                .collect(groupingByPairKeyAndCollectingValuesTo(groupingByPairKeyAndCollectingValuesToList()));

        groupedHandlerMethods.forEach((responseCode, typesGroupedByProduces) ->
                typesGroupedByProduces.forEach((producesContentTypes, methods) ->
                        mergedResponses.add(
                                new AbstractSpringWebHandlerMethod.SpringWebResponse(
                                        responseCode,
                                        ifEmptyUseSingleAllValue(producesContentTypes),
                                        mergeReturnTypes(methods)
                                ) {
                                    @Override
                                    public boolean shouldClearContent(Content content) {
                                        return groupedHandlerMethods.get(responseCode).size() == 1;
                                    }
                                }
                        )
                )
        );
        return mergedResponses;
    }

    private Optional<HandlerMethod.Type> mergeReturnTypes(List<SpringWebHandlerMethod> handlerMethods) {
        return handlerMethods.stream()
                .map(returnTypeMapper::getReturnType)
                .reduce(typeMerger::mergeType);
    }
}

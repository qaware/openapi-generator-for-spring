package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.MediaTypesProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class SpringWebHandlerMethodContextModifierFactory {

    private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;

    public HandlerMethod.ContextModifier<MapperContext> create(SpringWebHandlerMethod handlerMethod) {
        Set<String> consumesContentTypes = contentTypesMapper.findConsumesContentTypes(handlerMethod);
        Set<String> producesContentTypes = contentTypesMapper.findProducesContentTypes(handlerMethod);
        MediaTypesProvider mediaTypesProvider = owningType -> {
            if (RequestBody.class.equals(owningType)) {
                return consumesContentTypes;
            } else if (ApiResponse.class.equals(owningType)) {
                return producesContentTypes;
            }
            throw new IllegalStateException("Cannot provide media types for " + owningType.getSimpleName());
        };
        return mapperContext -> mapperContext.withMediaTypesProvider(mediaTypesProvider);
    }
}

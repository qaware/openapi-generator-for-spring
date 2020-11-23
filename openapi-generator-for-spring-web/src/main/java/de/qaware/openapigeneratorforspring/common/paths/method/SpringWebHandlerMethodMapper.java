package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.MediaTypesProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterMapper.RequestBodyParameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper.ifEmptyUseSingleAllValue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebHandlerMethodMapper {

    @RequiredArgsConstructor
    public static class ContextModifierMapper implements HandlerMethod.ContextModifierMapper<MapperContext> {

        private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;

        @Nullable
        @Override
        public HandlerMethod.ContextModifier<MapperContext> map(@Nullable HandlerMethod.Context context) {
            if (context instanceof SpringWebHandlerMethod) {
                SpringWebHandlerMethod handlerMethod = (SpringWebHandlerMethod) context;
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
            return null; // indicates we can't map this handler method context
        }
    }

    @RequiredArgsConstructor
    public static class RequestBodyMapper implements HandlerMethod.RequestBodyMapper {

        private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;
        private final SpringWebHandlerMethodRequestBodyParameterMapper requestBodyParameterMapper;

        @Nullable
        @Override
        public List<HandlerMethod.RequestBody> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof SpringWebHandlerMethod) {
                SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
                return requestBodyParameterMapper.findRequestBodyParameter(handlerMethod)
                        .map(requestBodyParameter -> buildSpringWebRequestBody(requestBodyParameter, springWebHandlerMethod))
                        .map(HandlerMethod.RequestBody.class::cast)
                        .map(Collections::singletonList)
                        .orElseGet(Collections::emptyList);
            }
            return null; // indicates we can't map this handler method instance
        }

        private AbstractSpringWebHandlerMethod.SpringWebRequestBody buildSpringWebRequestBody(RequestBodyParameter requestBodyParameter, SpringWebHandlerMethod handlerMethod) {
            return new AbstractSpringWebHandlerMethod.SpringWebRequestBody(
                    requestBodyParameter.getParameter().getAnnotationsSupplier(),
                    ifEmptyUseSingleAllValue(contentTypesMapper.findConsumesContentTypes(handlerMethod)),
                    requestBodyParameter.getParameter().getType(),
                    requestBodyParameter.isRequired()
            ) {
                @Override
                public HandlerMethod.Context getContext() {
                    return handlerMethod;
                }
            };
        }
    }

    @RequiredArgsConstructor
    public static class ResponseMapper implements HandlerMethod.ResponseMapper {

        private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;
        private final SpringWebHandlerMethodResponseCodeMapper responseCodeMapper;
        private final SpringWebHandlerMethodReturnTypeMapper returnTypeMapper;

        @Nullable
        @Override
        public List<HandlerMethod.Response> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof SpringWebHandlerMethod) {
                SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
                return Collections.singletonList(new AbstractSpringWebHandlerMethod.SpringWebResponse(
                        responseCodeMapper.getResponseCode(handlerMethod),
                        ifEmptyUseSingleAllValue(contentTypesMapper.findProducesContentTypes(handlerMethod)),
                        Optional.of(returnTypeMapper.getReturnType(springWebHandlerMethod))
                ));
            }
            return null; // indicates we can't map this handler method instance
        }
    }
}

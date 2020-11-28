package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterMapper.RequestBodyParameter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebHandlerMethodMapper {

    @RequiredArgsConstructor
    public static class ContextModifierMapper implements HandlerMethod.ContextModifierMapper<MapperContext> {

        private final SpringWebHandlerMethodContextModifierFactory contextModifierFactory;

        @Nullable
        @Override
        public HandlerMethod.ContextModifier<MapperContext> map(@Nullable HandlerMethod.Context context) {
            if (context instanceof SpringWebHandlerMethod) {
                return contextModifierFactory.create((SpringWebHandlerMethod) context);
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
                return requestBodyParameterMapper.findRequestBodyParameter(springWebHandlerMethod)
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
                    contentTypesMapper.findConsumesContentTypes(handlerMethod),
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
                        responseCodeMapper.getResponseCode(springWebHandlerMethod),
                        contentTypesMapper.findProducesContentTypes(springWebHandlerMethod),
                        Optional.of(returnTypeMapper.getReturnType(springWebHandlerMethod))
                ));
            }
            return null; // indicates we can't map this handler method instance
        }
    }
}

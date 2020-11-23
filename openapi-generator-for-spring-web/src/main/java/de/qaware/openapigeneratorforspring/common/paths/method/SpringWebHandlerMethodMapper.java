package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper.ifEmptyUseSingleAllValue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebHandlerMethodMapper {

    @RequiredArgsConstructor
    public static class RequestBodyMapper implements HandlerMethod.RequestBodyMapper {

        private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;
        private final SpringWebHandlerMethodRequestBodyParameterMapper requestBodyParameterMapper;

        @Nullable
        @Override
        public List<HandlerMethod.RequestBody> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof SpringWebHandlerMethod) {
                return requestBodyParameterMapper.findRequestBodyParameter(handlerMethod)
                        .map(requestBodyParameter -> new AbstractSpringWebHandlerMethod.SpringWebRequestBody(
                                requestBodyParameter.getParameter().getAnnotationsSupplier(),
                                ifEmptyUseSingleAllValue(contentTypesMapper.findConsumesContentTypes(handlerMethod)),
                                requestBodyParameter.getParameter().getType(),
                                requestBodyParameter.isRequired()
                        ))
                        .map(HandlerMethod.RequestBody.class::cast)
                        .map(Collections::singletonList)
                        .orElseGet(Collections::emptyList);
            }
            return null; // indicates we can't map this handler method instance
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

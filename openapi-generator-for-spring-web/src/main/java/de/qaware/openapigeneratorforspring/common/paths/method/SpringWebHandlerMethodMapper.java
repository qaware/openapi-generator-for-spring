/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Web
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebHandlerMethodMapper {

    @RequiredArgsConstructor
    public static class ContextModifierMapper implements HandlerMethod.ContextModifierMapper<MapperContext> {

        private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;

        @Nullable
        @Override
        public HandlerMethod.ContextModifier<MapperContext> map(@Nullable HandlerMethod.Context context) {
            if (context instanceof SpringWebHandlerMethod) {
                SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) context;
                MediaTypesProvider mediaTypesProvider = owningType -> {
                    if (RequestBody.class.equals(owningType)) {
                        return contentTypesMapper.findConsumesContentTypes(springWebHandlerMethod);
                    } else if (ApiResponse.class.equals(owningType)) {
                        return contentTypesMapper.findProducesContentTypes(springWebHandlerMethod);
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

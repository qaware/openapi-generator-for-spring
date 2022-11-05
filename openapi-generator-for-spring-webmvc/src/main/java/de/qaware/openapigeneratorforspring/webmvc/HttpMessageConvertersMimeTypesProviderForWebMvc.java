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

package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.ConsumesMimeTypeProvider;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.ProducesMimeTypeProvider;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.SpringWebRequestMappingAnnotationMimeTypesProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MimeType;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.laterThan;

@RequiredArgsConstructor
public class HttpMessageConvertersMimeTypesProviderForWebMvc implements ConsumesMimeTypeProvider, ProducesMimeTypeProvider {
    // give explicit annotations precedence before falling back to consulting HttpMessageConverters
    public static final int ORDER = laterThan(SpringWebRequestMappingAnnotationMimeTypesProvider.ORDER);

    private final HttpMessageConverters httpMessageConverters;
    private final SpringWebHandlerMethodRequestBodyParameterProvider requestBodyParameterProvider;

    @Override
    public Set<MimeType> findConsumesMimeTypes(HandlerMethod handlerMethod) {
        if (handlerMethod instanceof SpringWebHandlerMethod) {
            SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
            return requestBodyParameterProvider.findRequestBodyParameter(springWebHandlerMethod)
                    .map(requestBodyParameter -> findTypeFromSchemaAnnotationImplementation(requestBodyParameter.getParameter().getAnnotationsSupplier())
                            .map(schemaImplementationType -> getSupportedMediaTypes(schemaImplementationType, HttpMessageConverterWrapper::canRead))
                            .orElseGet(() -> getSupportedMediaTypes(requestBodyParameter.getParameter().getParameterType().getType(), HttpMessageConverterWrapper::canRead))
                    ).orElseGet(Collections::emptySet);
        }
        return Collections.emptySet();
    }

    @Override
    public Set<MimeType> findProducesMimeTypes(HandlerMethod handlerMethod) {
        return findTypeFromSchemaAnnotationImplementation(handlerMethod::findAnnotations)
                .map(schemaImplementationType -> getSupportedMediaTypes(schemaImplementationType, HttpMessageConverterWrapper::canWrite))
                .orElseGet(() -> {
                    if (handlerMethod instanceof SpringWebHandlerMethod) {
                        MethodParameter returnType = ((SpringWebHandlerMethod) handlerMethod).getMethod().getReturnType();
                        return getSupportedMediaTypes(ResolvableType.forMethodParameter(returnType), HttpMessageConverterWrapper::canWrite);
                    }
                    return Collections.emptySet();
                });
    }

    private Set<MimeType> getSupportedMediaTypes(ResolvableType type, BiPredicate<HttpMessageConverterWrapper, ResolvableType> canReadOrWrite) {
        Set<MimeType> result = new LinkedHashSet<>();
        for (HttpMessageConverter<?> converter : httpMessageConverters) {
            HttpMessageConverterWrapper wrapper = HttpMessageConverterWrapper.of(converter);
            if (canReadOrWrite.test(wrapper, type)) {
                result.addAll(wrapper.getSupportedMediaTypes(type.toClass()));
            }
        }
        return result;
    }

    private Optional<ResolvableType> findTypeFromSchemaAnnotationImplementation(AnnotationsSupplier annotationsSupplier) {
        return annotationsSupplier.findAnnotations(Schema.class)
                .<Class<?>>map(Schema::implementation)
                .filter(clazz -> !Void.class.equals(clazz))
                .findFirst()
                .map(ResolvableType::forClass);
    }

    @RequiredArgsConstructor
    private static class HttpMessageConverterWrapper {
        private final Function<Class<?>, List<MediaType>> getSupportedMediaTypes;
        private final Predicate<ResolvableType> canRead;
        private final Predicate<ResolvableType> canWrite;

        static HttpMessageConverterWrapper of(HttpMessageConverter<?> converter) {
            if (converter instanceof GenericHttpMessageConverter<?>) {
                GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                return new HttpMessageConverterWrapper(converter::getSupportedMediaTypes,
                        type -> genericConverter.canRead(type.getType(), null, null),
                        type -> genericConverter.canWrite(type.getType(), type.toClass(), null)
                );
            } else {
                return new HttpMessageConverterWrapper(converter::getSupportedMediaTypes,
                        type -> converter.canRead(type.toClass(), null),
                        type -> converter.canWrite(type.toClass(), null)
                );
            }
        }

        List<MediaType> getSupportedMediaTypes(Class<?> valueClass) {
            return getSupportedMediaTypes.apply(valueClass);
        }

        boolean canRead(ResolvableType type) {
            return canRead.test(type);
        }

        boolean canWrite(ResolvableType type) {
            return canWrite.test(type);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

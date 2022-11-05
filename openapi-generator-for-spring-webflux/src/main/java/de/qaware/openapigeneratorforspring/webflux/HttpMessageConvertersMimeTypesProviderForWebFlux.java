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

package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.ConsumesMimeTypeProvider;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.ProducesMimeTypeProvider;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.SpringWebRequestMappingAnnotationMimeTypesProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.util.MimeType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.laterThan;

@RequiredArgsConstructor
public class HttpMessageConvertersMimeTypesProviderForWebFlux implements ConsumesMimeTypeProvider, ProducesMimeTypeProvider {
    // give explicit annotations precedence before falling back to consulting readers/writers
    public static final int ORDER = laterThan(SpringWebRequestMappingAnnotationMimeTypesProvider.ORDER);

    private final List<HttpMessageReader<?>> httpMessageReaders;
    private final List<HttpMessageWriter<?>> httpMessageWriters;
    private final SpringWebHandlerMethodRequestBodyParameterProvider requestBodyParameterProvider;


    @Override
    public Set<MimeType> findConsumesMimeTypes(HandlerMethod handlerMethod) {
        if (handlerMethod instanceof SpringWebHandlerMethod) {
            SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
            return requestBodyParameterProvider.findRequestBodyParameter(springWebHandlerMethod)
                    .flatMap(requestBodyParameter -> findResolvableTypeFromSchemaAnnotationOrGet(
                            requestBodyParameter.getParameter().getAnnotationsSupplier(),
                            () -> requestBodyParameter.getParameter().getParameterType().getType()
                    )).map(resolvableType -> httpMessageReaders.stream()
                            .filter(reader -> reader.canRead(resolvableType, null))
                            .<MimeType>flatMap(reader -> reader.getReadableMediaTypes(resolvableType).stream())
                            .collect(Collectors.toSet())
                    )
                    .orElseGet(Collections::emptySet);
        }
        return Collections.emptySet();
    }

    @Override
    public Set<MimeType> findProducesMimeTypes(HandlerMethod handlerMethod) {
        return findResolvableTypeFromSchemaAnnotationOrGet(handlerMethod::findAnnotations,
                () -> Optional.of(handlerMethod)
                        .filter(SpringWebHandlerMethod.class::isInstance)
                        .map(SpringWebHandlerMethod.class::cast)
                        .map(SpringWebHandlerMethod::getMethod)
                        .filter(method -> !method.isVoid())
                        .map(org.springframework.web.method.HandlerMethod::getReturnType)
                        .map(ResolvableType::forMethodParameter)
                        .orElse(null)
        ).map(resolvableType -> httpMessageWriters.stream()
                .filter(decoder -> decoder.canWrite(resolvableType, null))
                .<MimeType>flatMap(decoder -> decoder.getWritableMediaTypes(resolvableType).stream())
                .collect(Collectors.toSet())
        ).orElseGet(Collections::emptySet);
    }

    private Optional<ResolvableType> findResolvableTypeFromSchemaAnnotationOrGet(AnnotationsSupplier annotationsSupplier, Supplier<ResolvableType> supplier) {
        Optional<ResolvableType> typeFromSchemaAnnotation = annotationsSupplier.findAnnotations(Schema.class)
                .<Class<?>>map(Schema::implementation)
                .filter(clazz -> !Void.class.equals(clazz))
                .findFirst()
                .map(ResolvableType::forClass);
        if (typeFromSchemaAnnotation.isPresent()) {
            return typeFromSchemaAnnotation;
        }
        return Optional.ofNullable(supplier.get());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

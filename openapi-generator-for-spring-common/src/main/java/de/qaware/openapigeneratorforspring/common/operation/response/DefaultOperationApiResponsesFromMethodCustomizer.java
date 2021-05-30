/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
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

package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver.Mode.FOR_SERIALIZATION;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;

@RequiredArgsConstructor
public class DefaultOperationApiResponsesFromMethodCustomizer implements OperationApiResponsesFromMethodCustomizer {

    static final String DEFAULT_ANNOTATION_RESPONSE_CODE = MergedAnnotation.of(io.swagger.v3.oas.annotations.responses.ApiResponse.class)
            .getDefaultValue("responseCode", String.class)
            .orElseThrow(() -> new IllegalStateException("Cannot infer default response code from ApiResponse annotation"));

    private final ApiResponseDefaultProvider apiResponseDefaultProvider;
    private final SchemaResolver schemaResolver;
    private final List<HandlerMethod.ResponseMapper> handlerMethodResponseMappers;

    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
        firstNonNull(handlerMethodResponseMappers, mapper -> mapper.map(handlerMethod)).ifPresent(handlerMethodResponses -> handlerMethodResponses.forEach(handlerMethodResponse -> {
            ApiResponse defaultApiResponse = getAndReplaceDefaultApiResponse(apiResponses, handlerMethodResponse.getResponseCode());
            Content content = Optional.ofNullable(defaultApiResponse.getContent()).orElseGet(Content::new);
            addMediaTypesToApiResponseContent(content, handlerMethod, handlerMethodResponse, operationBuilderContext.getReferencedItemConsumer(ReferencedSchemaConsumer.class));
            if (!content.isEmpty()) {
                defaultApiResponse.setContent(content);
            }
            handlerMethodResponse.customize(defaultApiResponse);
        }));
    }

    private void addMediaTypesToApiResponseContent(Content content, HandlerMethod handlerMethod, HandlerMethod.Response handlerMethodResponse, ReferencedSchemaConsumer referencedSchemaConsumer) {
        handlerMethodResponse.getProducesContentTypes().forEach(contentType -> {
            MediaType mediaType = content.getOrDefault(contentType, new MediaType());
            // we might have already set some media type, only amend this if the schema is not present
            // this gives annotations a higher preference than the schema inferred from the method return type
            if (mediaType.getSchema() == null) {
                handlerMethodResponse.getType().ifPresent(responseType -> {
                    AnnotationsSupplier annotationsSupplier = responseType.getAnnotationsSupplier()
                            // restrict searching the annotations from the handler method to @Schema or @ArraySchema only
                            // this prevents things like @Deprecated on the method to make the response schema also deprecated
                            // but we can still use @Schema to modify properties of the "default" response
                            .andThen(new AnnotationsSupplier() {
                                @Override
                                public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                                    if (annotationType.equals(Schema.class) || annotationType.equals(ArraySchema.class)) {
                                        return handlerMethod.findAnnotations(annotationType);
                                    }
                                    return Stream.empty();
                                }
                            });
                    schemaResolver.resolveFromType(FOR_SERIALIZATION, responseType.getType(), annotationsSupplier,
                            referencedSchemaConsumer,
                            mediaType::setSchema
                    );
                });
                // putting empty media types is ok
                // when there are multiple content types present for one response code
                content.put(contentType, mediaType);
            }
        });
    }

    private ApiResponse getAndReplaceDefaultApiResponse(ApiResponses apiResponses, String responseCodeFromMethod) {
        // we want to keep the place at which the default API response is inserted,
        // but we want to change the "default" response to the response code of the method
        AtomicReference<ApiResponse> defaultApiResponseHolder = new AtomicReference<>();
        ApiResponses modified = new ApiResponses();
        apiResponses.forEach((responseCode, apiResponse) -> {
            if (DEFAULT_ANNOTATION_RESPONSE_CODE.equals(responseCode) || responseCodeFromMethod.equals(responseCode)) {
                if (defaultApiResponseHolder.get() != null) {
                    // we cannot meaningfully merge those two API responses, so bail out
                    throw new IllegalStateException("Found default API response object for key " + DEFAULT_ANNOTATION_RESPONSE_CODE
                            + " and method code " + responseCodeFromMethod + ". Only one variant is supported for default response.");
                }
                defaultApiResponseHolder.set(apiResponse);
                modified.put(responseCodeFromMethod, apiResponse);
            } else {
                modified.put(responseCode, apiResponse);
            }
        });

        // update via given reference to original map
        apiResponses.clear();
        apiResponses.putAll(modified);

        // fallback to provider if default isn't present
        if (defaultApiResponseHolder.get() == null) {
            ApiResponse defaultApiResponse = apiResponseDefaultProvider.build(responseCodeFromMethod);
            apiResponses.put(responseCodeFromMethod, defaultApiResponse);
            return defaultApiResponse;
        }
        return defaultApiResponseHolder.get();
    }
}

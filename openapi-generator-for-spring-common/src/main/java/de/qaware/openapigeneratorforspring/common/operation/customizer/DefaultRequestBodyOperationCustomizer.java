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

package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferencedRequestBodyConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver.Caller.REQUEST_BODY;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultRequestBodyOperationCustomizer implements OperationCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final SchemaResolver schemaResolver;
    private final List<HandlerMethod.RequestBodyMapper> handlerMethodRequestBodyMappers;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        setIfNotEmpty(
                applyFromMethod(operation.getRequestBody(), operationBuilderContext),
                requestBody -> operationBuilderContext.getReferencedItemConsumer(ReferencedRequestBodyConsumer.class)
                        .maybeAsReference(requestBody, operation::setRequestBody)
        );
    }

    private RequestBody applyFromMethod(@Nullable RequestBody existingRequestBody, OperationBuilderContext operationBuilderContext) {
        return firstNonNull(handlerMethodRequestBodyMappers, mapper -> mapper.map(operationBuilderContext.getOperationInfo().getHandlerMethod()))
                .map(requestBodies -> buildRequestBody(requestBodies, existingRequestBody, operationBuilderContext))
                .orElseGet(RequestBody::new);
    }

    private RequestBody buildRequestBody(List<HandlerMethod.RequestBody> handlerMethodRequestBodies,
                                         @Nullable RequestBody existingRequestBody, OperationBuilderContext operationBuilderContext) {
        RequestBody requestBody = buildRequestBodyFromSwaggerAnnotations(handlerMethodRequestBodies, existingRequestBody, operationBuilderContext);
        handlerMethodRequestBodies.forEach(handlerMethodRequestBodyParameter -> {
            for (String contentType : handlerMethodRequestBodyParameter.getConsumesContentTypes()) {
                MediaType mediaType = addMediaTypeIfNotPresent(contentType, requestBody);
                if (mediaType.getSchema() == null) {
                    handlerMethodRequestBodyParameter.getType().ifPresent(parameterType -> schemaResolver.resolveFromType(
                            REQUEST_BODY,
                            parameterType.getType(),
                            handlerMethodRequestBodyParameter.getAnnotationsSupplier().andThen(parameterType.getAnnotationsSupplier()),
                            operationBuilderContext.getReferencedItemConsumer(ReferencedSchemaConsumer.class),
                            mediaType::setSchema
                    ));
                }
            }
            handlerMethodRequestBodyParameter.customize(requestBody);
        });
        return requestBody;
    }

    private RequestBody buildRequestBodyFromSwaggerAnnotations(List<HandlerMethod.RequestBody> handlerMethodRequestBodies, @Nullable RequestBody existingRequestBody, OperationBuilderContext operationBuilderContext) {
        RequestBody requestBody = Optional.ofNullable(existingRequestBody).orElseGet(RequestBody::new);
        handlerMethodRequestBodies.forEach(handlerMethodRequestBody ->
                handlerMethodRequestBody.getAnnotationsSupplier()
                        .findAnnotations(io.swagger.v3.oas.annotations.parameters.RequestBody.class)
                        .findFirst()
                        .ifPresent(swaggerRequestBodyAnnotation ->
                                requestBodyAnnotationMapper.applyFromAnnotation(requestBody, swaggerRequestBodyAnnotation,
                                        operationBuilderContext.getMapperContext(handlerMethodRequestBody.getContext())
                                )
                        )
        );
        return requestBody;
    }

    private static MediaType addMediaTypeIfNotPresent(String contentType, RequestBody requestBody) {
        if (requestBody.getContent() == null) {
            Content content = new Content();
            requestBody.setContent(content);
        }
        return requestBody.getContent().computeIfAbsent(contentType, ignored -> new MediaType());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

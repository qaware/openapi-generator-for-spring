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

package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.component.parameter.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferencedRequestBodyConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferencedApiResponsesConsumer;
import de.qaware.openapigeneratorforspring.common.reference.tag.ReferencedTagsConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultOperationAnnotationMapper implements OperationAnnotationMapper {

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ParameterAnnotationMapper parameterAnnotationMapper;
    private final ApiResponseAnnotationMapper apiResponseAnnotationMapper;
    private final SecurityRequirementAnnotationMapper securityRequirementAnnotationMapper;
    private final ServerAnnotationMapper serverAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Operation buildFromAnnotation(io.swagger.v3.oas.annotations.Operation operationAnnotation, MapperContext mapperContext) {
        Operation operation = new Operation();
        applyFromAnnotation(operation, operationAnnotation, mapperContext);
        return operation;
    }

    @Override
    public void applyFromAnnotation(Operation operation, io.swagger.v3.oas.annotations.Operation operationAnnotation, MapperContext mapperContext) {
        // operationAnnotation.method() ignored here, as its considered by caller when mapping callbacks
        setTags(operation, operationAnnotation.tags(), mapperContext);
        setStringIfNotBlank(operationAnnotation.summary(), operation::setSummary);
        setStringIfNotBlank(operationAnnotation.description(), operation::setDescription);
        setRequestBody(operation::setRequestBody, operationAnnotation.requestBody(), mapperContext);
        setIfNotEmpty(externalDocumentationAnnotationMapper.map(operationAnnotation.externalDocs()), operation::setExternalDocs);
        setStringIfNotBlank(operationAnnotation.operationId(), operation::setOperationId);
        setParameters(operation::setParameters, operationAnnotation.parameters(), mapperContext.withReferencedItemOwner(operation));
        setResponses(operation::setResponses, operationAnnotation.responses(), mapperContext);
        if (operationAnnotation.deprecated()) {
            operation.setDeprecated(true);
        }
        setMapIfNotEmpty(securityRequirementAnnotationMapper.mapArray(operationAnnotation.security()), operation::setFirstSecurity);
        setCollectionIfNotEmpty(serverAnnotationMapper.mapArray(operationAnnotation.servers()), operation::setServers);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(operationAnnotation.extensions()), operation::setExtensions);
    }

    private void setParameters(Consumer<List<Parameter>> setter, io.swagger.v3.oas.annotations.Parameter[] parameterAnnotations, MapperContext mapperContext) {
        setCollectionIfNotEmpty(
                Arrays.stream(parameterAnnotations)
                        .map(annotation -> parameterAnnotationMapper.buildFromAnnotation(annotation, mapperContext))
                        .filter(parameter -> !parameter.isEmpty())
                        .toList(),
                parameters -> mapperContext.getReferencedItemConsumer(ReferencedParametersConsumer.class).maybeAsReference(parameters, setter)
        );
    }

    private void setResponses(Consumer<ApiResponses> setter, ApiResponse[] apiResponseAnnotations, MapperContext mapperContext) {
        setMapIfNotEmpty(
                buildStringMapFromStream(Arrays.stream(apiResponseAnnotations),
                        ApiResponse::responseCode,
                        annotation -> apiResponseAnnotationMapper.buildFromAnnotation(annotation, mapperContext),
                        ApiResponses::new
                ),
                apiResponses -> mapperContext.getReferencedItemConsumer(ReferencedApiResponsesConsumer.class)
                        .maybeAsReference(apiResponses, setter)
        );
    }

    private void setRequestBody(Consumer<RequestBody> setter, io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation,
                                MapperContext mapperContext) {
        setIfNotEmpty(
                requestBodyAnnotationMapper.buildFromAnnotation(requestBodyAnnotation, mapperContext),
                requestBody -> mapperContext.getReferencedItemConsumer(ReferencedRequestBodyConsumer.class).maybeAsReference(requestBody, setter)
        );
    }

    private static void setTags(Operation operation, String[] tags, MapperContext mapperContext) {
        setCollectionIfNotEmpty(
                Stream.of(tags)
                        .filter(StringUtils::isNotBlank)
                        .distinct()
                        .toList(),
                tagNames -> {
                    // also consume tag names here, even if no description or additional information is present
                    // the tags consumer is smart enough to merge tags with identical names
                    mapperContext.getReferencedItemConsumer(ReferencedTagsConsumer.class).accept(
                            tagNames.stream()
                                    .map(tagName -> new Tag().withName(tagName))
                                    .toList()
                    );
                    operation.setTags(tagNames);
                }
        );

    }
}

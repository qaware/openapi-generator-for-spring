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

import de.qaware.openapigeneratorforspring.common.mapper.ApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferencedApiResponsesConsumer;
import de.qaware.openapigeneratorforspring.common.util.OpenApiProxyUtils;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationResponseCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ApiResponseAnnotationMapper apiResponseAnnotationMapper;
    private final List<OperationApiResponsesCustomizer> operationApiResponsesCustomizers;

    @Override
    public void customize(Operation operation, OperationBuilderContext context) {
        ApiResponses apiResponses = buildFromMethodAnnotations(operation, context);
        for (OperationApiResponsesCustomizer customizer : operationApiResponsesCustomizers) {
            customizer.customize(apiResponses, context);
        }
        ReferencedApiResponsesConsumer referencedApiResponsesConsumer = context.getReferencedItemConsumer(ReferencedApiResponsesConsumer.class);
        setMapIfNotEmpty(apiResponses, responses -> referencedApiResponsesConsumer.maybeAsReference(responses, operation::setResponses));
    }

    private ApiResponses buildFromMethodAnnotations(Operation operation, OperationBuilderContext operationBuilderContext) {
        ApiResponses apiResponses = Optional.ofNullable(operation.getResponses()).orElseGet(ApiResponses::new);

        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
        handlerMethod.findAnnotationsWithContext(io.swagger.v3.oas.annotations.responses.ApiResponse.class)
                .forEach((annotation, handlerMethodContext) -> {
                    String responseCode = annotation.responseCode();
                    if (StringUtils.isBlank(responseCode)) {
                        // at least it should be set to the "default" string
                        throw new IllegalStateException("Encountered ApiResponse annotation with empty response code");
                    }
                    ApiResponse apiResponse = apiResponses.computeIfAbsent(responseCode, ignored -> new ApiResponse());
                    ApiResponse smartImmutableApiResponse = OpenApiProxyUtils.smartImmutableProxy(apiResponse, OpenApiProxyUtils::addNonExistingKeys);
                    apiResponseAnnotationMapper.applyFromAnnotation(smartImmutableApiResponse, annotation,
                            operationBuilderContext.getMapperContext(handlerMethodContext)
                    );
                });

        return apiResponses;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}

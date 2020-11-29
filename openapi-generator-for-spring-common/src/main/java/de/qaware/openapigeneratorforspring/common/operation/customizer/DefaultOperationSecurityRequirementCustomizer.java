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

import de.qaware.openapigeneratorforspring.common.mapper.SecurityRequirementAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.ensureKeyIsNotBlank;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationSecurityRequirementCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final SecurityRequirementAnnotationMapper securityRequirementAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();

        // the following code assumes that all security requirements we find must be fulfilled at the same time,
        // it's more safe to assume that multiple requirements "add up" instead of requiring them as OR
        // the spec allows also an OR case, for which swagger annotation support seems to be missing though
        SecurityRequirement securityRequirement = handlerMethod.findAnnotations(io.swagger.v3.oas.annotations.security.SecurityRequirement.class)
                .map(securityRequirementAnnotationMapper::mapArray)
                .flatMap(x -> x.entrySet().stream())
                .collect(Collectors.toMap(ensureKeyIsNotBlank(Map.Entry::getKey), Map.Entry::getValue, (a, b) -> {
                    throw new IllegalStateException("Conflicting security requirement annotation with same name found: " + a + " vs. " + b);
                }, SecurityRequirement::new));
        // apply later from operation to overwrite them!
        securityRequirement.putAll(operation.getFirstSecurity().orElseGet(SecurityRequirement::new));
        setMapIfNotEmpty(securityRequirement, operation::setFirstSecurity);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

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

import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferencedCallbacksConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationCallbackCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final CallbackAnnotationMapper callbackAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
        setMapIfNotEmpty(
                buildStringMapFromStream(
                        handlerMethod.findAnnotations(io.swagger.v3.oas.annotations.callbacks.Callback.class),
                        io.swagger.v3.oas.annotations.callbacks.Callback::name,
                        // note that mapping the callback should ignore media types relevant for the operation,
                        // so we map here with null handler method context
                        callbackAnnotation -> callbackAnnotationMapper.map(callbackAnnotation, operationBuilderContext.getMapperContext(null))
                ),
                callbacks -> operationBuilderContext.getReferencedItemConsumer(ReferencedCallbacksConsumer.class)
                        .maybeAsReference(callbacks, operation::setCallbacks)
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

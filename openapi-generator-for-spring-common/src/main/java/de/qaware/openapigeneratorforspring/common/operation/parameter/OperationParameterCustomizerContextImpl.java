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

package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizerContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "of")
public class OperationParameterCustomizerContextImpl implements OperationParameterCustomizerContext {
    @Nullable
    private final HandlerMethod.Parameter handlerMethodParameter;
    private final OperationBuilderContext operationBuilderContext;

    @Override
    public Optional<HandlerMethod.Parameter> getHandlerMethodParameter() {
        return Optional.ofNullable(handlerMethodParameter);
    }

    @Override
    public OperationInfo getOperationInfo() {
        return operationBuilderContext.getOperationInfo();
    }

    @Override
    public MapperContext getMapperContext(@Nullable HandlerMethod.Context context) {
        return operationBuilderContext.getMapperContext(context);
    }

    @Override
    public <T extends ReferencedItemConsumer> T getReferencedItemConsumer(Class<T> referencedItemConsumerClazz) {
        return operationBuilderContext.getReferencedItemConsumer(referencedItemConsumerClazz);
    }
}

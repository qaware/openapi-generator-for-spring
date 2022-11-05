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

package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver.Caller.PARAMETER;

@RequiredArgsConstructor
public class DefaultOperationParameterSchemaCustomizer implements OperationParameterCustomizer {
    private final SchemaResolver schemaResolver;

    @Override
    public void customize(Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(handlerMethodParameter ->
                handlerMethodParameter.getType().ifPresent(parameterType -> {
                    ReferencedSchemaConsumer referencedSchemaConsumer = context.getReferencedItemConsumer(ReferencedSchemaConsumer.class);
                    AnnotationsSupplier annotationsSupplier = handlerMethodParameter.getAnnotationsSupplier()
                            .andThen(parameterType.getAnnotationsSupplier());
                    schemaResolver.resolveFromType(PARAMETER, parameterType.getType().getType(), annotationsSupplier, referencedSchemaConsumer, parameter::setSchema);
                })
        );
    }
}

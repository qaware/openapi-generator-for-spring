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

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
public class DefaultOperationParameterNullableCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(handlerMethodParameter -> {
            // TODO support more @Nullable / @NotNull annotations? combine with other places where @Nullable is checked?
            handlerMethodParameter.getAnnotationsSupplier()
                    .findAnnotations(Nullable.class).findFirst().ifPresent(ignored -> {
                Boolean required = parameter.getRequired();
                if (required != null && required) {
                    LOGGER.warn("{} in {} marked as required but annotated as @Nullable. Ignoring annotation.",
                            parameter, context.getOperationInfo());
                } else {
                    // TODO is this always right to explicitly set it to false?
                    parameter.setRequired(false);
                }
            });
        });
    }
}

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

import de.qaware.openapigeneratorforspring.common.mapper.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterAnnotationCustomizer implements OperationParameterCustomizer {
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customize(Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(
                handlerMethodParameter -> handlerMethodParameter.getAnnotationsSupplier()
                        .findAnnotations(io.swagger.v3.oas.annotations.Parameter.class).findFirst()
                        .ifPresent(parameterAnnotation ->
                                parameterAnnotationMapper.applyFromAnnotation(parameter, parameterAnnotation, context.getMapperContext(handlerMethodParameter.getContext()))
                        )
        );
    }
}

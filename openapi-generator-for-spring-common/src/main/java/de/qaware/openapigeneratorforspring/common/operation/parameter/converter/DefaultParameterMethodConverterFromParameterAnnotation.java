/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 - 2021 QAware GmbH
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

package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;
import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.muchLaterThan;

public class DefaultParameterMethodConverterFromParameterAnnotation implements ParameterMethodConverter {

    public static final int ORDER = muchLaterThan(DEFAULT_ORDER);

    @Nullable
    @Override
    public Parameter convert(HandlerMethod.Parameter handlerMethodParameter) {
        return handlerMethodParameter.getAnnotationsSupplier()
                .findAnnotations(io.swagger.v3.oas.annotations.Parameter.class)
                .findFirst()
                .flatMap(parameterAnnotation -> parameterAnnotation.hidden() ? Optional.empty() : Optional.of(new Parameter()))
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

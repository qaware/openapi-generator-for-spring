/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Web
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

package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ParameterMethodConverterFromAnnotation<A extends Annotation> implements ParameterMethodConverter {

    private final Class<A> annotationClass;

    @Nullable
    @Override
    public Parameter convert(HandlerMethod.Parameter handlerMethodParameter) {
        return handlerMethodParameter.getAnnotationsSupplier()
                .findAnnotations(annotationClass).findFirst()
                .map(this::buildParameter)
                .orElse(null);
    }

    @Nullable
    protected abstract Parameter buildParameter(A annotation);
}

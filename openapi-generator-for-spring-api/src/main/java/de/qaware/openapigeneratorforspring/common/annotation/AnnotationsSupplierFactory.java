/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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

package de.qaware.openapigeneratorforspring.common.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Factory for {@link AnnotationsSupplier}. Can be autowired as bean.
 */
public interface AnnotationsSupplierFactory {
    /**
     * Create annotations supplier from annotated element.
     * Prefer {@link #createFromMember} if possible.
     *
     * @param annotatedElement annotated element
     * @return annotations supplier
     */
    AnnotationsSupplier createFromAnnotatedElement(AnnotatedElement annotatedElement);

    /**
     * Create from Jackson annotated member.
     * Preferred over {@link #createFromAnnotatedElement} if possible.
     *
     * @param annotatedMember Jackson's annotated member
     * @return annotations supplier
     */
    AnnotationsSupplier createFromMember(AnnotatedMember annotatedMember);

    /**
     * Helper method to conveniently find annotations on {@link Method} including its declaring class.
     *
     * @param method method
     * @return annotations supplier
     */
    default AnnotationsSupplier createFromMethodWithDeclaringClass(Method method) {
        return createFromAnnotatedElement(method)
                .andThen(createFromAnnotatedElement(method.getDeclaringClass()));
    }
}

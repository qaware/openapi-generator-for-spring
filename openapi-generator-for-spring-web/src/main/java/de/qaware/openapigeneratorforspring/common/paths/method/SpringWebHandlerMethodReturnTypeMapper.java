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

package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class SpringWebHandlerMethodReturnTypeMapper {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public HandlerMethod.Type getReturnType(SpringWebHandlerMethod springWebHandlerMethod) {
        Method method = springWebHandlerMethod.getMethod().getMethod();
        // even for Void method return type, there might still be @Schema annotation which could be useful
        // using method.getReturnType() does not work for generic return types
        return AbstractSpringWebHandlerMethod.SpringWebType.of(method.getGenericReturnType(), annotationsSupplierFactory.createFromAnnotatedElement(method.getReturnType()));
    }
}

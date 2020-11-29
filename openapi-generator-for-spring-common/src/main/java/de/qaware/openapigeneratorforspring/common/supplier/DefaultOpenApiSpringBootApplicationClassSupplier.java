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

package de.qaware.openapigeneratorforspring.common.supplier;

import lombok.Setter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DefaultOpenApiSpringBootApplicationClassSupplier implements OpenApiSpringBootApplicationClassSupplier, ApplicationContextAware {
    @Setter
    @Nullable
    private ApplicationContext applicationContext;

    @Override
    public Optional<Class<?>> findSpringBootApplicationClass() {
        return Optional.ofNullable(applicationContext)
                .map(context -> applicationContext.getBeansWithAnnotation(SpringBootApplication.class))
                .map(Map::values)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one SpringBootApplication annotated bean");
                })
                .map(Object::getClass)
                // remove Spring proxy classes
                .map(ClassUtils::getUserClass);
    }
}

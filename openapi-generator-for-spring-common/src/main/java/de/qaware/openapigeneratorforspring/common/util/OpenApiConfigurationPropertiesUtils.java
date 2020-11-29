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

package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiConfigurationPropertiesUtils {

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public abstract static class ConfigurationPropertyCondition<T> extends SpringBootCondition {
        private final Class<T> propertiesClass;
        private final Predicate<T> predicate;

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return new ConditionOutcome(predicate.test(bindProperties(context)), (String) null);
        }

        private T bindProperties(ConditionContext context) {
            return Binder.get(context.getEnvironment())
                    .bind(ConfigurationPropertyName.of(findPrefixFromClass()), Bindable.of(propertiesClass))
                    .orElseGet(() -> {
                        try {
                            return propertiesClass.getDeclaredConstructor().newInstance();
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                            throw new IllegalStateException("Cannot create instance of " + propertiesClass);
                        }
                    });
        }

        private String findPrefixFromClass() {
            ConfigurationProperties annotation = AnnotationUtils.findAnnotation(propertiesClass, ConfigurationProperties.class);
            if (annotation == null) {
                throw new IllegalStateException("Cannot find @ConfigurationProperties on " + propertiesClass);
            }
            return annotation.prefix();
        }
    }
}

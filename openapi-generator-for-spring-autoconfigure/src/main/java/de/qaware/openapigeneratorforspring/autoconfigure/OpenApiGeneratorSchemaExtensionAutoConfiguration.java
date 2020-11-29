/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Autoconfigure
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

package de.qaware.openapigeneratorforspring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForValidation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.DefaultJava8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;

@EnableConfigurationProperties(Java8TimeTypeResolverConfigurationProperties.class)
public class OpenApiGeneratorSchemaExtensionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Java8TimeInitialSchemaBuilder defaultJava8TimeInitialSchemaBuilder(
            Java8TimeTypeResolverConfigurationProperties properties,
            // Use Spring Web's object mapper bean
            @Nullable ObjectMapper objectMapper
    ) {
        return new DefaultJava8TimeInitialSchemaBuilder(properties, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Min.class)
    public SchemaCustomizerForValidation defaultSchemaCustomizerForValidation() {
        return new SchemaCustomizerForValidation();
    }
}

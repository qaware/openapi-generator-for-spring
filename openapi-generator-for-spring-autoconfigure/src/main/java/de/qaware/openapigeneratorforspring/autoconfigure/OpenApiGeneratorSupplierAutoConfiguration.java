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

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiSpringBootApplicationClassSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiYamlMapper;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationClassSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiYamlMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorSupplierAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public OpenApiObjectMapperSupplier defaultOpenApiObjectMapperSupplier() {
        return new DefaultOpenApiObjectMapperSupplier();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSpringBootApplicationClassSupplier defaultOpenApiSpringBootApplicationClassSupplier() {
        return new DefaultOpenApiSpringBootApplicationClassSupplier();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSpringBootApplicationAnnotationsSupplier defaultOpenApiSpringBootApplicationAnnotationsSupplier(
            OpenApiSpringBootApplicationClassSupplier springBootApplicationClassSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOpenApiSpringBootApplicationAnnotationsSupplier(springBootApplicationClassSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAPIDefinitionAnnotationSupplier defaultOpenAPIDefinitionAnnotationSupplier(
            OpenApiSpringBootApplicationAnnotationsSupplier openApiSpringBootApplicationAnnotationsSupplier
    ) {
        return new DefaultOpenAPIDefinitionAnnotationSupplier(openApiSpringBootApplicationAnnotationsSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiServersSupplier defaultOpenApiServersSupplier(
            OpenApiBaseUriSupplier openApiBaseUriSupplier // provided by WebMVC or WebFlux
    ) {
        return new DefaultOpenApiServersSupplier(openApiBaseUriSupplier);
    }

    @ConditionalOnClass(YAMLFactory.class)
    @Bean
    public OpenApiYamlMapper defaultOpenApiYamlMapper() {
        return new DefaultOpenApiYamlMapper();
    }
}

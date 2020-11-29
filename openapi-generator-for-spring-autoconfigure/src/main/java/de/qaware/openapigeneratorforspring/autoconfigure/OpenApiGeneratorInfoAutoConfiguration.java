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

import de.qaware.openapigeneratorforspring.common.info.DefaultOpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.info.DefaultOpenApiVersionSupplier;
import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.info.OpenApiVersionSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.InfoAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationClassSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(OpenApiInfoConfigurationProperties.class)
public class OpenApiGeneratorInfoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiInfoSupplier defaultOpenApiInfoSupplier(
            OpenApiInfoConfigurationProperties infoProperties,
            InfoAnnotationMapper infoAnnotationMapper,
            OpenApiVersionSupplier openApiVersionSupplier,
            OpenApiSpringBootApplicationClassSupplier springBootApplicationClassSupplier,
            OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier
    ) {
        return new DefaultOpenApiInfoSupplier(infoProperties, infoAnnotationMapper, openApiVersionSupplier,
                springBootApplicationClassSupplier, openAPIDefinitionAnnotationSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiVersionSupplier defaultOpenApiVersionSupplier() {
        return new DefaultOpenApiVersionSupplier();
    }

}

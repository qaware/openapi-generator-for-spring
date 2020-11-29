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

import de.qaware.openapigeneratorforspring.common.DefaultOpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
        OpenApiGeneratorFilterAutoConfiguration.class,
        OpenApiGeneratorInfoAutoConfiguration.class,
        OpenApiGeneratorMapperAutoConfiguration.class,
        OpenApiGeneratorOperationAutoConfiguration.class,
        OpenApiGeneratorPathsAutoConfiguration.class,
        OpenApiGeneratorReferenceAutoConfiguration.class,
        OpenApiGeneratorSchemaAutoConfiguration.class,
        OpenApiGeneratorSupplierAutoConfiguration.class,
        OpenApiGeneratorUtilAutoConfiguration.class,
})
@EnableConfigurationProperties(OpenApiConfigurationProperties.class)
public class OpenApiGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiGenerator openApiGenerator(
            PathsBuilder pathsBuilder,
            ReferencedItemSupportFactory referencedItemSupportFactory,
            List<OpenApiCustomizer> openApiCustomizers
    ) {
        return new OpenApiGenerator(pathsBuilder, referencedItemSupportFactory, openApiCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOpenApiCustomizer defaultOpenApiCustomizer(
            OpenApiInfoSupplier openApiInfoSupplier,
            ServerAnnotationMapper serverAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            List<OpenApiServersSupplier> openApiServersSuppliers,
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier,
            OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier
    ) {
        return new DefaultOpenApiCustomizer(
                serverAnnotationMapper,
                externalDocumentationAnnotationMapper,
                extensionAnnotationMapper,
                openApiInfoSupplier,
                openApiServersSuppliers,
                springBootApplicationAnnotationsSupplier,
                openAPIDefinitionAnnotationSupplier
        );
    }
}

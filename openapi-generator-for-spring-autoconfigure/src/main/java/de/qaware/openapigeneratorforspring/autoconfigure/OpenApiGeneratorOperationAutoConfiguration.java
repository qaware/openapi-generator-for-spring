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

import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.SecurityRequirementAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultDeprecatedOperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationCallbackCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationIdCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationSecurityRequirementCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationSecuritySchemesCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationServersCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationTagsCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultRequestBodyOperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorOperationParameterAutoConfiguration.class,
        OpenApiGeneratorOperationResponseAutoConfiguration.class
})
public class OpenApiGeneratorOperationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OperationBuilder operationBuilder(
            OperationAnnotationMapper operationAnnotationMapper,
            List<OperationCustomizer> operationCustomizers,
            List<HandlerMethod.ContextModifierMapper<MapperContext>> contextModifierMappersForMapperContext
    ) {
        return new OperationBuilder(operationAnnotationMapper, operationCustomizers, contextModifierMappersForMapperContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationIdProvider defaultOperationIdProvider() {
        return new DefaultOperationIdProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationIdConflictResolver defaultOperationIdConflictResolver() {
        return new DefaultOperationIdConflictResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultDeprecatedOperationCustomizer defaultDeprecatedOperationCustomizer() {
        return new DefaultDeprecatedOperationCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationTagsCustomizer defaultOperationTagsCustomizer(TagAnnotationMapper tagAnnotationMapper) {
        return new DefaultOperationTagsCustomizer(tagAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationSecuritySchemesCustomizer defaultOperationSecuritySchemesCustomizer(SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper) {
        return new DefaultOperationSecuritySchemesCustomizer(securitySchemeAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationServersCustomizer defaultOperationServersCustomizer(ServerAnnotationMapper serverAnnotationMapper) {
        return new DefaultOperationServersCustomizer(serverAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationSecurityRequirementCustomizer defaultOperationSecurityRequirementCustomizer(SecurityRequirementAnnotationMapper securityRequirementAnnotationMapper) {
        return new DefaultOperationSecurityRequirementCustomizer(securityRequirementAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationIdCustomizer defaultOperationIdCustomizer(OperationIdProvider operationIdProvider) {
        return new DefaultOperationIdCustomizer(operationIdProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationCallbackCustomizer defaultOperationCallbackCustomizer(CallbackAnnotationMapper callbackAnnotationMapper) {
        return new DefaultOperationCallbackCustomizer(callbackAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultRequestBodyOperationCustomizer defaultRequestBodyOperationCustomizer(
            RequestBodyAnnotationMapper requestBodyAnnotationMapper,
            SchemaResolver schemaResolver,
            List<HandlerMethod.RequestBodyMapper> handlerMethodRequestBodyMappers
    ) {
        return new DefaultRequestBodyOperationCustomizer(requestBodyAnnotationMapper, schemaResolver, handlerMethodRequestBodyMappers);
    }
}

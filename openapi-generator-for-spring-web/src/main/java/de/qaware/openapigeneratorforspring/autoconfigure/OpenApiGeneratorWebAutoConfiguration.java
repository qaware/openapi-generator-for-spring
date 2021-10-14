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

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.parameter.DefaultOpenApiSpringWebParameterNameDiscoverer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.OpenApiSpringWebParameterNameDiscoverer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromMatrixVariableAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromPathVariableAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestHeaderAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestParamAnnotation;
import de.qaware.openapigeneratorforspring.common.paths.DefaultSpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodEnumMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.spring.SpringWebResponseEntityInitialTypeBuilder;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiYamlMapper;
import de.qaware.openapigeneratorforspring.common.web.OpenApiResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.util.Optional;

@Import({
        OpenApiGeneratorWebMethodMergerAutoConfiguration.class,
        OpenApiGeneratorWebMethodAutoConfiguration.class
})
public class OpenApiGeneratorWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResource openApiResource(
            OpenApiGenerator openApiGenerator,
            OpenApiConfigurationProperties openApiConfigurationProperties,
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            Optional<OpenApiYamlMapper> openApiYamlMapper
    ) {
        return new OpenApiResource(openApiGenerator, openApiConfigurationProperties, openApiObjectMapperSupplier, openApiYamlMapper.orElse(null));
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder(
            AnnotationsSupplierFactory annotationsSupplierFactory,
            OpenApiSpringWebParameterNameDiscoverer openApiSpringWebParameterNameDiscoverer
    ) {
        return new DefaultSpringWebHandlerMethodBuilder(annotationsSupplierFactory, openApiSpringWebParameterNameDiscoverer);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSpringWebParameterNameDiscoverer defaultOpenApiSpringWebParameterNameDiscoverer() {
        return new DefaultOpenApiSpringWebParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromPathVariableAnnotation defaultParameterMethodConverterFromPathVariableAnnotation() {
        return new DefaultParameterMethodConverterFromPathVariableAnnotation();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestHeaderAnnotation defaultParameterMethodConverterFromRequestHeaderAnnotation() {
        return new DefaultParameterMethodConverterFromRequestHeaderAnnotation();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestParamAnnotation defaultParameterMethodConverterFromRequestParamAnnotation() {
        return new DefaultParameterMethodConverterFromRequestParamAnnotation();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromMatrixVariableAnnotation defaultParameterMethodConverterFromMatrixVariableAnnotation() {
        return new DefaultParameterMethodConverterFromMatrixVariableAnnotation();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebResponseEntityInitialTypeBuilder springWebResponseEntityInitialTypeBuilder() {
        return new SpringWebResponseEntityInitialTypeBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper() {
        return new SpringWebRequestMethodEnumMapper();
    }
}

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

package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromPathVariableAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestHeaderAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestParamAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.SpringWebOpenApiParameterBuilder;
import de.qaware.openapigeneratorforspring.common.paths.DefaultSpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodEnumMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.spring.SpringWebResponseEntityTypeResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({OpenApiGeneratorWebMethodMergerAutoConfiguration.class, OpenApiGeneratorWebMethodAutoConfiguration.class})
public class OpenApiGeneratorWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new DefaultSpringWebHandlerMethodBuilder(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder() {
        return new SpringWebOpenApiParameterBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromPathVariableAnnotation defaultParameterMethodConverterFromPathVariableAnnotation(SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder) {
        return new DefaultParameterMethodConverterFromPathVariableAnnotation(springWebOpenApiParameterBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestHeaderAnnotation defaultParameterMethodConverterFromRequestHeaderAnnotation(SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder) {
        return new DefaultParameterMethodConverterFromRequestHeaderAnnotation(springWebOpenApiParameterBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestParamAnnotation defaultParameterMethodConverterFromRequestParamAnnotation(SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder) {
        return new DefaultParameterMethodConverterFromRequestParamAnnotation(springWebOpenApiParameterBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebResponseEntityTypeResolver defaultDefaultSpringResponseEntityTypeResolver() {
        return new SpringWebResponseEntityTypeResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper() {
        return new SpringWebRequestMethodEnumMapper();
    }
}

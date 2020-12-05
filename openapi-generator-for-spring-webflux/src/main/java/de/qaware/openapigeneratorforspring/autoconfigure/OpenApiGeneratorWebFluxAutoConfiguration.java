/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebFlux
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
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodEnumMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForFlux;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForFlux;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilderForMono;
import de.qaware.openapigeneratorforspring.common.web.OpenApiResource;
import de.qaware.openapigeneratorforspring.webflux.HandlerMethodsProviderForWebFlux;
import de.qaware.openapigeneratorforspring.webflux.OpenApiBaseUriSupplierForWebFlux;
import de.qaware.openapigeneratorforspring.webflux.OpenApiRequestAwareProviderForWebFlux;
import de.qaware.openapigeneratorforspring.webflux.OpenApiResourceForWebFlux;
import de.qaware.openapigeneratorforspring.webflux.function.RouterFunctionHandlerMethodWithInfoBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Import(OpenApiGeneratorWebFluxRouterFunctionAutoConfiguration.class)
public class OpenApiGeneratorWebFluxAutoConfiguration {

    @Bean
    @Conditional(OpenApiConfigurationProperties.EnabledCondition.class)
    public InitializingBean openApiResourceRegistration(
            OpenApiResourceForWebFlux openApiResourceForWebFlux,
            RequestMappingHandlerMapping requestMappingHandlerMapping
    ) {
        return () -> openApiResourceForWebFlux.registerMapping(
                (path, produces) -> RequestMappingInfo.paths(path).methods(GET).produces(produces).build(),
                requestMappingHandlerMapping::registerMapping
        );
    }

    @Bean
    public OpenApiResourceForWebFlux openApiResourceForWebFlux(OpenApiResource openApiResource) {
        return new OpenApiResourceForWebFlux(openApiResource);
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodsProvider handlerMethodsProviderFromWebFlux(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder,
            SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper,
            Map<String, RouterFunction<?>> routerFunctions,
            RouterFunctionHandlerMethodWithInfoBuilder routerFunctionHandlerMethodWithInfoBuilder
    ) {
        return new HandlerMethodsProviderForWebFlux(
                requestMappingHandlerMapping, springWebHandlerMethodBuilder, springWebRequestMethodEnumMapper,
                routerFunctions, routerFunctionHandlerMethodWithInfoBuilder
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiRequestAwareProviderForWebFlux openApiRequestAwareProviderForWebFlux() {
        return new OpenApiRequestAwareProviderForWebFlux();
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForFlux defaultTypeResolverForFlux(InitialSchemaBuilderForFlux initialSchemaBuilder) {
        return new TypeResolverForFlux(initialSchemaBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForFlux defaultInitialSchemaBuilderForFlux() {
        return new InitialSchemaBuilderForFlux();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialTypeBuilderForMono defaultInitialTypeBuilderForMono() {
        return new InitialTypeBuilderForMono();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiBaseUriSupplierForWebFlux openApiBaseUriProviderForWebFlux(OpenApiConfigurationProperties openApiConfigurationProperties) {
        return new OpenApiBaseUriSupplierForWebFlux(openApiConfigurationProperties);
    }
}

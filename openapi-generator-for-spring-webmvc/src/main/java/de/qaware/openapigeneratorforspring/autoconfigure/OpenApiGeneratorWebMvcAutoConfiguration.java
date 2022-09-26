/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebMVC
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
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterProvider;
import de.qaware.openapigeneratorforspring.common.web.OpenApiResource;
import de.qaware.openapigeneratorforspring.webmvc.HandlerMethodPathPatternsProviderForWebMvc;
import de.qaware.openapigeneratorforspring.webmvc.HandlerMethodsProviderForWebMvc;
import de.qaware.openapigeneratorforspring.webmvc.HttpMessageConvertersMimeTypesProviderForWebMvc;
import de.qaware.openapigeneratorforspring.webmvc.OpenApiRequestAwareSupplierForWebMvc;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiGeneratorWebMvcAutoConfiguration {

    @Bean
    @Conditional(OpenApiConfigurationProperties.EnabledCondition.class)
    public InitializingBean openApiResourceRegistration(
            OpenApiResource openApiResource,
            RequestMappingHandlerMapping requestMappingHandlerMapping
    ) {
        return () -> openApiResource.registerMapping(
                (path, produces) -> RequestMappingInfo
                        .paths(path)
                        .methods(GET)
                        .produces(produces)
                        .options(requestMappingHandlerMapping.getBuilderConfiguration())
                        .build(),
                requestMappingHandlerMapping::registerMapping,
                openApiResource
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder,
            SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper,
            HandlerMethodPathPatternsProviderForWebMvc handlerMethodPathPatternsProviderForWebMvc
    ) {
        return new HandlerMethodsProviderForWebMvc(requestMappingHandlerMapping, springWebHandlerMethodBuilder, springWebRequestMethodEnumMapper, handlerMethodPathPatternsProviderForWebMvc);
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodPathPatternsProviderForWebMvc handlerMethodPathPatternsProviderForWebMvc() {
        return new HandlerMethodPathPatternsProviderForWebMvc();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiRequestAwareSupplierForWebMvc openApiRequestAwareProviderForWebMvc(
            WebRequest webRequest,
            HttpServletRequest httpServletRequest
    ) {
        return new OpenApiRequestAwareSupplierForWebMvc(webRequest, httpServletRequest);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConvertersMimeTypesProviderForWebMvc springBootHttpMessageConvertersMimeTypesProvider(
            HttpMessageConverters httpMessageConverters,
            SpringWebHandlerMethodRequestBodyParameterProvider springWebHandlerMethodRequestBodyParameterProvider
    ) {
        return new HttpMessageConvertersMimeTypesProviderForWebMvc(httpMessageConverters, springWebHandlerMethodRequestBodyParameterProvider);
    }
}

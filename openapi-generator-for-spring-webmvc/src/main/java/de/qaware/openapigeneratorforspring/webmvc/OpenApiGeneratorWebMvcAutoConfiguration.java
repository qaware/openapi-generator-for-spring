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

package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodEnumMapper;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiGeneratorWebMvcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Conditional(OpenApiConfigurationProperties.EnabledCondition.class)
    public OpenApiResourceForWebMvc openApiResource(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier openApiObjectMapperSupplier) {
        return new OpenApiResourceForWebMvc(openApiGenerator, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder,
            SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper
    ) {
        return new HandlerMethodsProviderForWebMvc(requestMappingHandlerMapping, springWebHandlerMethodBuilder, springWebRequestMethodEnumMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiRequestAwareSupplierForWebMvc openApiRequestAwareProviderForWebMvc(
            WebRequest webRequest,
            HttpServletRequest httpServletRequest
    ) {
        return new OpenApiRequestAwareSupplierForWebMvc(webRequest, httpServletRequest);
    }
}

/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: UI
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

import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.swagger.OpenApiSwaggerUiCsrfSupport;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarTransformedResourceBuilder;
import de.qaware.openapigeneratorforspring.ui.webmvc.OpenApiSwaggerUiCsrfSupportProviderForWebMvc;
import de.qaware.openapigeneratorforspring.ui.webmvc.WebJarResourceTransformerSupportFactoryForWebMvc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Conditional(OpenApiSwaggerUiConfigurationProperties.EnabledCondition.class)
@Import(OpenApiSwaggerUiWebMvcAutoConfiguration.CsrfConfiguration.class)
public class OpenApiSwaggerUiWebMvcAutoConfiguration {

    @Bean
    public WebJarResourceTransformerSupportFactoryForWebMvc webJarResourceTransformersFactoryForWebMvc(
            List<WebJarResourceTransformerFactory> webJarResourceTransformerFactories,
            OpenApiBaseUriSupplier openApiBaseUriSupplier,
            WebJarTransformedResourceBuilder transformedResourceBuilder,
            Optional<OpenApiSwaggerUiCsrfSupportProviderForWebMvc> openApiCsrfSupportProviderForWebMvc
    ) {
        return new WebJarResourceTransformerSupportFactoryForWebMvc(webJarResourceTransformerFactories, openApiBaseUriSupplier,
                transformedResourceBuilder, openApiCsrfSupportProviderForWebMvc.orElse(null));
    }

    @ConditionalOnClass(CsrfToken.class)
    static class CsrfConfiguration {
        @Bean
        public OpenApiSwaggerUiCsrfSupportProviderForWebMvc openApiCsrfSupportProviderForWebFlux(ServletRequest servletRequest) {
            return () -> {
                Object csrfTokenAttribute = servletRequest.getAttribute(CsrfToken.class.getName());
                if (csrfTokenAttribute instanceof CsrfToken) {
                    CsrfToken csrfToken = (CsrfToken) csrfTokenAttribute;
                    return OpenApiSwaggerUiCsrfSupport.of(csrfToken.getHeaderName(), csrfToken.getToken());
                }
                return null;
            };
        }
    }

    @Bean
    public WebMvcConfigurer swaggerUiWebMvcConfigurer(
            SwaggerUiSupport swaggerUiSupport,
            OpenApiSwaggerUiConfigurationProperties uiProperties,
            WebJarResourceTransformerSupportFactoryForWebMvc transformerSupportFactory
    ) {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(swaggerUiSupport.getUiPath() + "/**")
                        .addResourceLocations(swaggerUiSupport.getWebJarClassPath())
                        .resourceChain(uiProperties.isCacheUiResources())
                        .addTransformer((request, inputResource, transformerChain) ->
                                transformResourceIfMatching(transformerChain.transform(request, inputResource))
                        );
            }

            private Resource transformResourceIfMatching(Resource outputResource) throws IOException {
                return transformerSupportFactory.create().transformResourceIfMatching(outputResource,
                        () -> new BufferedReader(new InputStreamReader(outputResource.getInputStream(), StandardCharsets.UTF_8)).lines()
                                .collect(Collectors.joining(System.lineSeparator())));
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                String redirectToIndexHtml = swaggerUiSupport.getRedirectPath();
                registry.addRedirectViewController(swaggerUiSupport.getUiPath(), redirectToIndexHtml);
                // additional trailing slash redirect is required for WebMVC
                registry.addRedirectViewController(swaggerUiSupport.getUiPath() + DEFAULT_PATH_SEPARATOR, redirectToIndexHtml);
            }
        };
    }
}

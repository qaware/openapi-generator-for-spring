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

import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport;
import de.qaware.openapigeneratorforspring.ui.webflux.WebJarResourceTransformerSupportFactoryForWebFlux;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarTransformedResourceBuilder;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Conditional(OpenApiSwaggerUiConfigurationProperties.EnabledCondition.class)
public class OpenApiSwaggerUiWebFluxAutoConfiguration {

    @Bean
    public WebJarResourceTransformerSupportFactoryForWebFlux webJarResourceTransformersFactoryForWebFlux(
            List<WebJarResourceTransformerFactory> webJarResourceTransformerFactories,
            WebJarTransformedResourceBuilder transformedResourceBuilder
    ) {
        return new WebJarResourceTransformerSupportFactoryForWebFlux(webJarResourceTransformerFactories, transformedResourceBuilder);
    }

    @Bean
    @Hidden
    public RouterFunction<ServerResponse> redirectToSwaggerUiIndexHtml(SwaggerUiSupport swaggerUiSupport) {
        return route(GET(swaggerUiSupport.getUiPath()), req ->
                ServerResponse.status(HttpStatus.FOUND)
                        .location(req.uriBuilder().replacePath(swaggerUiSupport.getRedirectPath()).build())
                        .build()
        );
    }

    @Bean
    public WebFluxConfigurer swaggerUiWebFluxConfigurer(
            SwaggerUiSupport swaggerUiSupport,
            OpenApiSwaggerUiConfigurationProperties uiProperties,
            WebJarResourceTransformerSupportFactoryForWebFlux transformerSupportFactory
    ) {
        String classPathToSwaggerUi = swaggerUiSupport.getWebJarClassPath();
        return new WebFluxConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(swaggerUiSupport.getUiPath() + "/**")
                        .addResourceLocations(classPathToSwaggerUi)
                        .resourceChain(uiProperties.isCacheUiResources())
                        .addTransformer((exchange, inputResource, transformerChain) -> transformerChain.transform(exchange, inputResource)
                                .flatMap(outputResource -> transformResourceIfMatching(exchange, outputResource))
                        );
            }

            private Mono<Resource> transformResourceIfMatching(ServerWebExchange exchange, Resource outputResource) {
                return transformerSupportFactory.create(exchange).transformResourceIfMatching(outputResource, Mono::just, (transformer, resourceBuilder) ->
                        DataBufferUtils.join(DataBufferUtils.read(outputResource, exchange.getResponse().bufferFactory(), StreamUtils.BUFFER_SIZE))
                                .map(dataBuffer -> {
                                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
                                    String transformedContent = transformer.apply(charBuffer.toString());
                                    DataBufferUtils.release(dataBuffer);
                                    return resourceBuilder.apply(transformedContent);
                                })
                );
            }
        };
    }
}

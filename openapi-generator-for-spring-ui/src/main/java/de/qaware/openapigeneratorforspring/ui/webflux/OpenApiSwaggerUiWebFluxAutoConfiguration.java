package de.qaware.openapigeneratorforspring.ui.webflux;

import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
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
import org.springframework.web.reactive.resource.TransformedResource;
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
    public WebJarResourceTransformerSupportFactoryForWebFlux webJarResourceTransformersFactoryForWebFlux(List<WebJarResourceTransformerFactory> webJarResourceTransformerFactories) {
        return new WebJarResourceTransformerSupportFactoryForWebFlux(webJarResourceTransformerFactories);
    }

    @Bean
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
            WebJarResourceTransformerSupportFactoryForWebFlux transformerSupportFactory
    ) {
        String classPathToSwaggerUi = swaggerUiSupport.getWebJarClassPath();
        return new WebFluxConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(swaggerUiSupport.getUiPath() + "/**")
                        .addResourceLocations(classPathToSwaggerUi)
                        .resourceChain(false) // TODO investigate if caching should really be disabled
                        .addTransformer((exchange, inputResource, transformerChain) -> transformerChain.transform(exchange, inputResource)
                                .flatMap(outputResource -> transformResourceIfMatching(exchange, outputResource))
                        );
            }

            private Mono<Resource> transformResourceIfMatching(ServerWebExchange exchange, Resource outputResource) {
                return transformerSupportFactory.create(exchange).transformResourceIfMatching(outputResource, Mono::just, transformer ->
                        DataBufferUtils.join(DataBufferUtils.read(outputResource, exchange.getResponse().bufferFactory(), StreamUtils.BUFFER_SIZE))
                                .map(dataBuffer -> {
                                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
                                    String transformedContent = transformer.transform(charBuffer.toString());
                                    DataBufferUtils.release(dataBuffer);
                                    return new TransformedResource(outputResource, transformedContent.getBytes());
                                })
                );
            }
        };
    }
}

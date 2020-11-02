package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.webflux.OpenApiBaseUriSupplierForWebFlux;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.resource.TransformedResource;
import org.springframework.web.util.UriComponentsBuilder;
import org.webjars.WebJarAssetLocator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiAutoConfiguration.INDEX_HTML_FILE;
import static de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiAutoConfiguration.SWAGGER_UI_WEB_JAR;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Conditional(OpenApiSwaggerUiConfigurationProperties.EnabledCondition.class)
public class OpenApiSwaggerUiWebFluxAutoConfiguration {

    @Bean
    public RouterFunction<ServerResponse> redirectToSwaggerUiIndexHtml(OpenApiSwaggerUiConfigurationProperties swaggerUiProperties) {
        String redirectToIndexHtmlPath = swaggerUiProperties.getPath() + DEFAULT_PATH_SEPARATOR + INDEX_HTML_FILE;
        return route(GET(swaggerUiProperties.getPath()), req -> ServerResponse.status(HttpStatus.FOUND)
                .location(req.uriBuilder().replacePath(redirectToIndexHtmlPath).build())
                .build()
        );
    }

    @Bean
    public RouterFunction<ServerResponse> redirectToSwaggerUiIndexHtmlTrailingSlash(OpenApiSwaggerUiConfigurationProperties swaggerUiProperties) {
        String redirectToIndexHtmlPath = swaggerUiProperties.getPath() + DEFAULT_PATH_SEPARATOR + INDEX_HTML_FILE;
        return route(GET(swaggerUiProperties.getPath() + DEFAULT_PATH_SEPARATOR), req -> ServerResponse.status(HttpStatus.FOUND)
                .location(req.uriBuilder().replacePath(redirectToIndexHtmlPath).build())
                .build()
        );
    }

    @Bean
    public WebFluxConfigurer swaggerUiWebFluxConfigurer(
            OpenApiConfigurationProperties properties,
            OpenApiSwaggerUiConfigurationProperties swaggerUiProperties
    ) {

        String pathToSwaggerUiIndexHtml = new WebJarAssetLocator().getFullPath(SWAGGER_UI_WEB_JAR, INDEX_HTML_FILE);
        String pathToSwaggerUi = pathToSwaggerUiIndexHtml.substring(0, pathToSwaggerUiIndexHtml.length() - INDEX_HTML_FILE.length());

        return new WebFluxConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(swaggerUiProperties.getPath() + "/**")
                        .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + pathToSwaggerUi)
                        .resourceChain(false) // TODO investigate if caching should really be disabled
                        .addTransformer((exchange, inputResource, transformerChain) -> transformerChain.transform(exchange, inputResource).flatMap(outputResource -> {
                            if (!(outputResource instanceof ClassPathResource)) {
                                return Mono.just(outputResource);
                            }
                            ClassPathResource classPathResource = (ClassPathResource) outputResource;
                            if (!classPathResource.getPath().endsWith(INDEX_HTML_FILE)) {
                                return Mono.just(outputResource);
                            }
                            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
                            Flux<DataBuffer> flux = DataBufferUtils.read(outputResource, bufferFactory, StreamUtils.BUFFER_SIZE);
                            return DataBufferUtils.join(flux)
                                    .map(dataBuffer -> {
                                        String apiDocsUri = UriComponentsBuilder.fromUriString(OpenApiBaseUriSupplierForWebFlux.getBaseUri(exchange))
                                                .path(DEFAULT_PATH_SEPARATOR + properties.getApiDocsPath())
                                                .build().toUriString();
                                        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
                                        DataBufferUtils.release(dataBuffer);
                                        String modifiedIndexHtmlContent = charBuffer.toString()
                                                // TODO one should probably replace this with proper templating (use Mustache?)
                                                .replace("https://petstore.swagger.io/v2/swagger.json", apiDocsUri);
                                        return new TransformedResource(outputResource, modifiedIndexHtmlContent.getBytes());
                                    });
                        }));
            }
        };
    }
}

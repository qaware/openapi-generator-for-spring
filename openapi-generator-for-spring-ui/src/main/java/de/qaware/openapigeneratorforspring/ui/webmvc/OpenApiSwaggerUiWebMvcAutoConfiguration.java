package de.qaware.openapigeneratorforspring.ui.webmvc;

import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarTransformedResourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Conditional(OpenApiSwaggerUiConfigurationProperties.EnabledCondition.class)
public class OpenApiSwaggerUiWebMvcAutoConfiguration {

    @Bean
    public WebJarResourceTransformerSupportFactoryForWebMvc webJarResourceTransformersFactoryForWebMvc(
            List<WebJarResourceTransformerFactory> webJarResourceTransformerFactories,
            OpenApiBaseUriSupplier openApiBaseUriSupplier,
            WebJarTransformedResourceBuilder transformedResourceBuilder
    ) {
        return new WebJarResourceTransformerSupportFactoryForWebMvc(webJarResourceTransformerFactories, openApiBaseUriSupplier, transformedResourceBuilder);
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

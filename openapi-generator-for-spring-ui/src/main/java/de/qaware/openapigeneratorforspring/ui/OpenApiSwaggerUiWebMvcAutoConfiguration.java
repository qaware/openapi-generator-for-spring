package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.util.OpenApiBaseUriProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.TransformedResource;
import org.springframework.web.util.UriComponentsBuilder;
import org.webjars.WebJarAssetLocator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiAutoConfiguration.INDEX_HTML_FILE;
import static de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiAutoConfiguration.SWAGGER_UI_WEB_JAR;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Conditional(OpenApiSwaggerUiConfigurationProperties.EnabledCondition.class)
public class OpenApiSwaggerUiWebMvcAutoConfiguration {

    @Bean
    public WebMvcConfigurer swaggerUiWebMvcConfigurer(
            OpenApiConfigurationProperties properties,
            OpenApiSwaggerUiConfigurationProperties swaggerUiProperties,
            OpenApiBaseUriProvider openApiBaseUriProvider
    ) {

        String pathToSwaggerUiIndexHtml = new WebJarAssetLocator().getFullPath(SWAGGER_UI_WEB_JAR, INDEX_HTML_FILE);
        String pathToSwaggerUi = pathToSwaggerUiIndexHtml.substring(0, pathToSwaggerUiIndexHtml.length() - INDEX_HTML_FILE.length());

        return new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(swaggerUiProperties.getPath() + "/**")
                        .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + pathToSwaggerUi)
                        .resourceChain(false) // TODO investigate if caching should really be disabled
                        .addTransformer((request, resource, transformerChain) -> {
                            if (resource.getURL().getPath().endsWith(INDEX_HTML_FILE)) {
                                String apiDocsUri = UriComponentsBuilder.fromUriString(openApiBaseUriProvider.getBaseUri())
                                        .path(DEFAULT_PATH_SEPARATOR + properties.getApiDocsPath())
                                        .build().toUriString();
                                String modifiedIndexHtmlContent = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines()
                                        .collect(Collectors.joining(System.lineSeparator()))
                                        // TODO one should probably replace this with proper templating (use Mustache?)
                                        .replace("https://petstore.swagger.io/v2/swagger.json", apiDocsUri);
                                return new TransformedResource(resource, modifiedIndexHtmlContent.getBytes());
                            }
                            return resource;
                        });
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                String redirectToIndexHtml = swaggerUiProperties.getPath() + DEFAULT_PATH_SEPARATOR + INDEX_HTML_FILE;
                registry.addRedirectViewController(swaggerUiProperties.getPath(), redirectToIndexHtml);
                registry.addRedirectViewController(swaggerUiProperties.getPath() + DEFAULT_PATH_SEPARATOR, redirectToIndexHtml);
            }
        };
    }
}

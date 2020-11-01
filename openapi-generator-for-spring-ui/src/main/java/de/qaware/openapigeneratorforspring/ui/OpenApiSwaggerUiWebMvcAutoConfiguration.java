package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.TransformedResource;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.webjars.WebJarAssetLocator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiSwaggerUiWebMvcAutoConfiguration {

    public static final String INDEX_HTML_FILE = "index.html";
    public static final String SWAGGER_UI_WEB_JAR = "swagger-ui";

    @Bean
    public WebMvcConfigurer swaggerUiWebMvcConfigurer(OpenApiSwaggerUiConfigurationProperties uiProperties, OpenApiConfigurationProperties properties) {
        String pathToSwaggerUiIndexHtml = new WebJarAssetLocator().getFullPath(SWAGGER_UI_WEB_JAR, INDEX_HTML_FILE);
        String pathToSwaggerUi = pathToSwaggerUiIndexHtml.substring(0, pathToSwaggerUiIndexHtml.length() - INDEX_HTML_FILE.length());
        return new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(uiProperties.getPath() + "/**")
                        .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + pathToSwaggerUi)
                        .resourceChain(false) // TODO investigate if caching should really be disabled
                        .addTransformer((request, resource, transformerChain) -> {
                            if (resource.getURL().getPath().endsWith(INDEX_HTML_FILE)) {
                                String apiDocsUri = ServletUriComponentsBuilder.fromContextPath(request)
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
                String redirectToIndexHtml = uiProperties.getPath() + DEFAULT_PATH_SEPARATOR + INDEX_HTML_FILE;
                registry.addRedirectViewController(uiProperties.getPath(), redirectToIndexHtml);
                registry.addRedirectViewController(uiProperties.getPath() + DEFAULT_PATH_SEPARATOR, redirectToIndexHtml);
            }
        };
    }
}

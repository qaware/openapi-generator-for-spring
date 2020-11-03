package de.qaware.openapigeneratorforspring.ui.swagger;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport.INDEX_HTML_FILE;

@RequiredArgsConstructor
public class SwaggerUiIndexHtmlWebJarResourceTransformer implements WebJarResourceTransformer {
    private final UriComponents baseUri;
    private final OpenApiConfigurationProperties properties;

    @Override
    public boolean matches(Resource resource) {
        if (resource instanceof ClassPathResource) {
            ClassPathResource classPathResource = (ClassPathResource) resource;
            return classPathResource.getPath().endsWith(INDEX_HTML_FILE);
        }
        return false;
    }

    @Override
    public String transform(String resourceContent) {
        String apiDocsUri = UriComponentsBuilder.fromUri(baseUri.toUri())
                // .path appends to the base uri path
                .path(properties.getApiDocsPath())
                .build().toUriString();
        return resourceContent
                // TODO one should probably replace this with proper templating (use Mustache?)
                .replace("https://petstore.swagger.io/v2/swagger.json", apiDocsUri);
    }
}

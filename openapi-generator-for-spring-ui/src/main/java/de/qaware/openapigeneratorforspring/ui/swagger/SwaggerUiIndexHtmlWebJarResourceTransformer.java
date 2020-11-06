package de.qaware.openapigeneratorforspring.ui.swagger;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport.INDEX_HTML_FILE;

@RequiredArgsConstructor
public class SwaggerUiIndexHtmlWebJarResourceTransformer implements WebJarResourceTransformer {
    private final URI baseUri;
    private final OpenApiConfigurationProperties properties;
    private final OpenApiSwaggerUiApiDocsUrisSupplier swaggerUiApiDocsUrisSupplier;

    @Override
    public boolean matches(Resource resource) {
        if (resource instanceof ClassPathResource) {
            ClassPathResource classPathResource = (ClassPathResource) resource;
            return classPathResource.getPath().endsWith(INDEX_HTML_FILE);
        }
        return false;
    }

    @Override
    public String apply(String resourceContent) {
        URI apiDocsUri = UriComponentsBuilder.fromUri(baseUri)
                // .path appends to the base uri path and thus preserves a possible context path
                .path(properties.getApiDocsPath())
                .build().toUri();
        String apiDocsUrisJsonArray = swaggerUiApiDocsUrisSupplier.getApiDocsUris(apiDocsUri).stream()
                .map(item -> "{url: \"" + item.getApiDocsUri() + "\", name: \"" + item.getName() + "\"}")
                .collect(Collectors.joining(",", "[", "]"));
        return resourceContent
                // TODO one should probably replace this with proper templating (use Mustache?)
                .replace("url: \"https://petstore.swagger.io/v2/swagger.json\"",
                        // important that url: "" is kept, removing it breaks the Swagger UI
                        "url:\"\",\n urls: " + apiDocsUrisJsonArray);
    }
}

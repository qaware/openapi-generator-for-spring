package de.qaware.openapigeneratorforspring.ui.swagger;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@RequiredArgsConstructor
public class SwaggerUiIndexHtmlWebJarResourceTransformerFactory implements WebJarResourceTransformerFactory {

    private final OpenApiConfigurationProperties properties;
    private final OpenApiSwaggerUiApiDocsUrisSupplier swaggerUiApiDocsUrisSupplier;

    @Override
    public WebJarResourceTransformer create(URI baseUri) {
        return new SwaggerUiIndexHtmlWebJarResourceTransformer(baseUri, properties, swaggerUiApiDocsUrisSupplier);
    }
}

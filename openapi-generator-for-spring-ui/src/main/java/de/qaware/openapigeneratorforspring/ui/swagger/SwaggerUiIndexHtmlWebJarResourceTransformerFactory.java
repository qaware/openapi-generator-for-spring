package de.qaware.openapigeneratorforspring.ui.swagger;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponents;

@RequiredArgsConstructor
public class SwaggerUiIndexHtmlWebJarResourceTransformerFactory implements WebJarResourceTransformerFactory {

    private final OpenApiConfigurationProperties properties;

    @Override
    public WebJarResourceTransformer create(UriComponents baseUri) {
        return new SwaggerUiIndexHtmlWebJarResourceTransformer(baseUri, properties);
    }
}

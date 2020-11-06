package de.qaware.openapigeneratorforspring.ui.webflux;

import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarTransformedResourceBuilder;
import de.qaware.openapigeneratorforspring.webflux.OpenApiBaseUriSupplierForWebFlux;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WebJarResourceTransformerSupportFactoryForWebFlux {

    private final List<WebJarResourceTransformerFactory> resourceTransformerFactories;
    private final WebJarTransformedResourceBuilder transformedResourceBuilder;

    WebJarResourceTransformerSupport create(ServerWebExchange exchange) {
        URI baseUri = OpenApiBaseUriSupplierForWebFlux.getBaseUri(exchange);
        List<WebJarResourceTransformer> transformers = resourceTransformerFactories.stream()
                .map(factory -> factory.create(baseUri))
                .collect(Collectors.toList());
        return new WebJarResourceTransformerSupport(transformers, transformedResourceBuilder);
    }
}

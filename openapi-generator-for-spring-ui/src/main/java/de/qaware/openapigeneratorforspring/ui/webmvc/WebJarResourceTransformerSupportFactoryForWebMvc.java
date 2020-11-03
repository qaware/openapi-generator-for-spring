package de.qaware.openapigeneratorforspring.ui.webmvc;

import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponents;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WebJarResourceTransformerSupportFactoryForWebMvc {

    private final List<WebJarResourceTransformerFactory> resourceTransformerFactories;
    private final OpenApiBaseUriSupplier openApiBaseUriSupplier;

    WebJarResourceTransformerSupport create() {
        UriComponents baseUri = openApiBaseUriSupplier.getBaseUri();
        List<WebJarResourceTransformer> transformers = resourceTransformerFactories.stream()
                .map(factory -> factory.create(baseUri))
                .collect(Collectors.toList());
        return new WebJarResourceTransformerSupport(transformers);
    }
}

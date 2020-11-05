package de.qaware.openapigeneratorforspring.common.supplier;

import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultOpenApiDefaultServerSupplier implements OpenApiDefaultServersSupplier {
    private final OpenApiBaseUriSupplier openApiBaseUriSupplier;

    @Override
    public List<Server> get() {
        URI baseUri = openApiBaseUriSupplier.getBaseUri();
        if (StringUtils.isBlank(baseUri.getPath()) || baseUri.getPath().trim().equals("/")) {
            return Collections.emptyList();
        }
        return Collections.singletonList(Server.builder()
                .description("Default Server")
                .url(baseUri.toString())
                .build()
        );
    }
}

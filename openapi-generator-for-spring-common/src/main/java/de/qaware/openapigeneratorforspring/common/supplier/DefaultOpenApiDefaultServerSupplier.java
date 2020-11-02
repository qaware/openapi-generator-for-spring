package de.qaware.openapigeneratorforspring.common.supplier;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultOpenApiDefaultServerSupplier implements OpenApiServersSupplier {
    private final OpenApiBaseUriSupplier openApiBaseUriSupplier;
    private final OpenApiConfigurationProperties properties;

    @Override
    public List<Server> get() {
        if (!properties.getServer().isAddDefault()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(Server.builder()
                .description("Default Server")
                .url(openApiBaseUriSupplier.getBaseUri())
                .build()
        );
    }
}

package de.qaware.openapigeneratorforspring.common.server;

import de.qaware.openapigeneratorforspring.common.util.OpenApiBaseUriProvider;
import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultOpenApiDefaultServerSupplier implements OpenApiDefaultServerSupplier {
    private final OpenApiBaseUriProvider openApiBaseUriProvider;

    @Override
    public List<Server> get() {
        return Collections.singletonList(Server.builder()
                .description("Default Server")
                .url(openApiBaseUriProvider.getBaseUri())
                .build()
        );
    }
}

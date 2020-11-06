package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultOpenApiSwaggerUiApiDocsUrisSupplier implements OpenApiSwaggerUiApiDocsUrisSupplier {

    private final OpenApiConfigurationProperties properties;

    @Override
    public List<ApiDocsUriWithName> getApiDocsUris(URI apiDocsUri) {
        return Collections.singletonList(ApiDocsUriWithName.of("Default", apiDocsUri));
    }
}

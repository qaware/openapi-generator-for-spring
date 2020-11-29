package de.qaware.openapigeneratorforspring.ui;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class DefaultOpenApiSwaggerUiApiDocsUrisSupplier implements OpenApiSwaggerUiApiDocsUrisSupplier {
    @Override
    public List<ApiDocsUriWithName> getApiDocsUris(URI apiDocsUri) {
        return Collections.singletonList(ApiDocsUriWithName.of("Default", apiDocsUri));
    }
}

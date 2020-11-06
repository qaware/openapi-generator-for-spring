package de.qaware.openapigeneratorforspring.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

public interface OpenApiSwaggerUiApiDocsUrisSupplier {

    List<ApiDocsUriWithName> getApiDocsUris(URI apiDocsUri);

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    class ApiDocsUriWithName {
        private final String name;
        private final URI apiDocsUri;
    }
}

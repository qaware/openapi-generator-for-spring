package de.qaware.openapigeneratorforspring.ui.swagger;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier.ApiDocsUriWithName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SwaggerUiIndexHtmlWebJarResourceTransformerTest {

    private static final String IGNORED_CONTENT = "ignored";
    private static final String BASE_URI = "http://base-uri";
    private static final String API_DOCS_PATH = "/api-docs-path";
    private static final String API_DOCS_URI = BASE_URI + API_DOCS_PATH;

    private SwaggerUiIndexHtmlWebJarResourceTransformer sut;

    @Mock
    private OpenApiConfigurationProperties properties;
    @Mock
    private OpenApiSwaggerUiApiDocsUrisSupplier swaggerUiApiDocsUrisSupplier;

    @BeforeEach
    void setUp() throws Exception {
        when(properties.getApiDocsPath()).thenReturn(API_DOCS_PATH);
        sut = new SwaggerUiIndexHtmlWebJarResourceTransformer(URI.create(BASE_URI),
                OpenApiSwaggerUiCsrfSupport.of("X-CSRF-TOKEN", "SOME-CSRF-TOKEN"),
                properties, swaggerUiApiDocsUrisSupplier
        );
    }

    @Test
    void apply_noUrl() throws Exception {
        String actual = sut.apply(IGNORED_CONTENT);
        Assertions.assertThat(actual).isNotBlank();
    }

    @Test
    void apply_oneUrl() throws Exception {
        URI apiDocsUriWithQuery = URI.create(API_DOCS_URI + "?some=query");
        when(swaggerUiApiDocsUrisSupplier.getApiDocsUris(URI.create(API_DOCS_URI)))
                .thenReturn(Collections.singletonList(ApiDocsUriWithName.of("name1", apiDocsUriWithQuery)));
        String actual = sut.apply(IGNORED_CONTENT);
        Assertions.assertThat(actual)
                .contains("url: \"http://base-uri/api-docs-path?some=query\"")
                .doesNotContain("urls", "name1", "StandaloneLayout", "SwaggerUIStandalonePreset");
    }

    @Test
    void apply_twoUrls() throws Exception {
        URI apiDocsUri = URI.create(API_DOCS_URI);
        URI apiDocsUri1 = URI.create(API_DOCS_URI + "?query1");
        URI apiDocsUri2 = URI.create(API_DOCS_URI + "?query2");
        when(swaggerUiApiDocsUrisSupplier.getApiDocsUris(apiDocsUri))
                .thenReturn(Arrays.asList(ApiDocsUriWithName.of("name1", apiDocsUri1), ApiDocsUriWithName.of("name2", apiDocsUri2)));
        String actual = sut.apply(IGNORED_CONTENT);
        Assertions.assertThat(actual).contains("name1", apiDocsUri1.toString(), "name2", apiDocsUri2.toString(),
                "StandaloneLayout", "SwaggerUIStandalonePreset");
    }
}

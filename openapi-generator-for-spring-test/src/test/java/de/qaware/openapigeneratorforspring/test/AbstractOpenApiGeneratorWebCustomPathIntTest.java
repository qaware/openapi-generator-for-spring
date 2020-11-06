package de.qaware.openapigeneratorforspring.test;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiIndexHtmlWebJarResourceTransformerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestPropertySource(properties = {
        "openapi-generator-for-spring.api-docs-path=/my/own-path/to-api-docs",
        "openapi-generator-for-spring.ui.path=/my/own-path/to-swagger-ui",
        "server.forward-headers-strategy = framework"
})
public abstract class AbstractOpenApiGeneratorWebCustomPathIntTest extends AbstractOpenApiGeneratorWebIntTest {

    private static final String PATH_TO_SWAGGER_UI = "/my/own-path/to-swagger-ui";
    private static final String PATH_TO_INDEX_HTML = PATH_TO_SWAGGER_UI + "/index.html";
    protected static final String PATH_TO_API_DOCS = "/my/own-path/to-api-docs";

    @Autowired
    private OpenApiConfigurationProperties properties;
    @SpyBean
    private SwaggerUiIndexHtmlWebJarResourceTransformerFactory transformerFactory;

    @Test
    public void testSwaggerUiIndexHtmlWithCustomPath() throws Exception {
        assertThat(getResponseBody(PATH_TO_INDEX_HTML)).contains(buildHost() + PATH_TO_API_DOCS);
        assertThat(getResponseBody(PATH_TO_INDEX_HTML)).isNotBlank();
        // no ui resource caching means it's called 2 times
        verify(transformerFactory, times(2)).create(any());
    }

    @Test
    public void testSwaggerUiRedirectToIndexHtml() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("x-forwarded-host", singletonList("forwarded-host"));
        httpHeaders.put("x-forwarded-port", singletonList("1337"));
        httpHeaders.put("x-forwarded-proto", singletonList("https"));
        assertRedirectEntity(getResponseEntity(PATH_TO_SWAGGER_UI, httpHeaders));
        assertRedirectEntity(getResponseEntity(PATH_TO_SWAGGER_UI + "/", httpHeaders));
    }

    private void assertRedirectEntity(ResponseEntity<String> redirectEntity) {
        assertThat(redirectEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(redirectEntity.getHeaders()).contains(entry("Location",
                singletonList(buildHost("https://forwarded-host:1337") + PATH_TO_INDEX_HTML)
        ));
    }

    @Test
    public void testConfigurationPropertyIsSet() throws Exception {
        // we're using SPEL in the controller's @RequestMapping,
        // so make sure it's also working with configuration properties
        assertThat(properties.getApiDocsPath()).isEqualTo(PATH_TO_API_DOCS);
    }
}

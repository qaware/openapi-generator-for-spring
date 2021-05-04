package de.qaware.openapigeneratorforspring.test.app29;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebIntTest;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiIndexHtmlWebJarResourceTransformerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestPropertySource(properties = {
        "openapi-generator-for-spring.ui.cache-ui-resources=true",
        "spring.main.web-application-type = REACTIVE"
})
class App29Test extends AbstractOpenApiGeneratorWebIntTest {

    @SpyBean
    private SwaggerUiIndexHtmlWebJarResourceTransformerFactory transformerFactory;

    @Test
    void testSwaggerUiIndexHtmlResourceIsCached() throws Exception {
        assertThat(getResponseBody("/swagger-ui/index.html")).isNotBlank();
        assertThat(getResponseBody("/swagger-ui/index.html")).contains(CSRF_TOKEN_HEADER_NAME);
        // with caching, resource is only transformed once
        verify(transformerFactory, times(1)).create(any(), any());
    }
}

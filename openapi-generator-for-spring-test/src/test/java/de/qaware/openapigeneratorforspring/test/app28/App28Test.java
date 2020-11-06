package de.qaware.openapigeneratorforspring.test.app28;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebIntTest;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiIndexHtmlWebJarResourceTransformerFactory;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "openapi-generator-for-spring.ui.cache-ui-resources=true")
public class App28Test extends AbstractOpenApiGeneratorWebIntTest {

    @SpyBean
    private SwaggerUiIndexHtmlWebJarResourceTransformerFactory transformerFactory;

    @Test
    public void testSwaggerUiIndexHtmlResourceIsCached() throws Exception {
        assertThat(getResponseBody("/swagger-ui/index.html")).isNotBlank();
        assertThat(getResponseBody("/swagger-ui/index.html")).isNotBlank();
        // with caching, resource is only transformed once
        verify(transformerFactory, times(1)).create(any());
    }
}

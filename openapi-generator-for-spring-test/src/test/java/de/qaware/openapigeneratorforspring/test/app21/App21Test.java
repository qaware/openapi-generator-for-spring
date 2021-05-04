package de.qaware.openapigeneratorforspring.test.app21;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebCustomPathIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static de.qaware.openapigeneratorforspring.test.OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiJson;


@TestPropertySource(properties = "server.servlet.context-path=/my-context-path")
class App21Test extends AbstractOpenApiGeneratorWebCustomPathIntTest {
    @Test
    void testWithCustomApiDocsPath() throws Exception {
        assertMatchesOpenApiJson("app21.json", () ->
                getResponseBody(PATH_TO_API_DOCS).replace(buildHost(), "http://localhost/my-context-path")
        );
    }
}

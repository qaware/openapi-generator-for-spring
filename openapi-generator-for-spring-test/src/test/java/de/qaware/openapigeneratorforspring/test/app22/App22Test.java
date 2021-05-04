package de.qaware.openapigeneratorforspring.test.app22;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebCustomPathIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static de.qaware.openapigeneratorforspring.test.OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiJson;

@TestPropertySource(properties = "spring.main.web-application-type = REACTIVE")
class App22Test extends AbstractOpenApiGeneratorWebCustomPathIntTest {
    @Test
    void testWithCustomApiDocsPath() throws Exception {
        assertMatchesOpenApiJson("app22.json", () ->
                getResponseBody(PATH_TO_API_DOCS).replace(buildHost(), "http://localhost")
        );
    }
}

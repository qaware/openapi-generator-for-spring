package de.qaware.openapigeneratorforspring.test.app21;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@TestPropertySource(properties = "openapi-generator-for-spring.api-docs-path=/my/own-path/to-api-docs")
public class App21Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Autowired
    private OpenApiConfigurationProperties properties;

    @Test
    public void testWithCustomApiDocsPath() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app21.json",
                mockMvc.perform(get("/my/own-path/to-api-docs"))
        );
    }

    @Test
    public void testConfigurationPropertyIsSet() throws Exception {
        // we're using SPEL in the controller's @RequestMapping,
        // so make sure it's also working with configuration properties
        assertThat(properties.getApiDocsPath()).isEqualTo("/my/own-path/to-api-docs");
    }
}

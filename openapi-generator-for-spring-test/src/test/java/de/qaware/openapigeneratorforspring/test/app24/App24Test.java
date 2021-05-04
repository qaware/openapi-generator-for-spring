package de.qaware.openapigeneratorforspring.test.app24;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "openapi-generator-for-spring.enabled=false")
class App24Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Test
    void testOpenApiIsNotFoundWhenDisabled() throws Exception {
        performApiDocsRequest(x -> x)
                .andExpect(status().isNotFound());
    }
}

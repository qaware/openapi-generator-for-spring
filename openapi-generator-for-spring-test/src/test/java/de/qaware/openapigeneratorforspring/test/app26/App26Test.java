package de.qaware.openapigeneratorforspring.test.app26;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "openapi-generator-for-spring.ui.enabled=false")
class App26Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Test
    void testOpenApiIsNotFoundWhenDisabled() throws Exception {
        performApiDocsRequest(x -> x)
                .andExpect(status().isOk());
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isNotFound());
    }
}

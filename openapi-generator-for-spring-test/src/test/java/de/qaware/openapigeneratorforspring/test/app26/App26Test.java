package de.qaware.openapigeneratorforspring.test.app26;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "openapi-generator-for-spring.ui.enabled=false")
public class App26Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Test
    public void testOpenApiIsNotFoundWhenDisabled() throws Exception {
        performApiDocsRequest(x -> x)
                .andExpect(status().isOk());
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isNotFound());
    }
}

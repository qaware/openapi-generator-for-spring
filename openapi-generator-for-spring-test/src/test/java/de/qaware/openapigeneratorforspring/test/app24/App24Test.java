package de.qaware.openapigeneratorforspring.test.app24;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "openapi-generator-for-spring.enabled=false")
public class App24Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Test
    public void testOpenApiIsNotFoundWhenDisabled() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isNotFound());
    }
}

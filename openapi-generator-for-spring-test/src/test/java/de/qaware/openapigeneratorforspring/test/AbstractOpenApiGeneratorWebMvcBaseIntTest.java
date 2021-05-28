package de.qaware.openapigeneratorforspring.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Autowired
    protected MockMvc mockMvc;

    protected ResultActions performApiDocsRequest(Function<MockHttpServletRequestBuilder, MockHttpServletRequestBuilder> requestModifier) throws Exception {
        return mockMvc.perform(requestModifier.apply(get("/v3/api-docs")));
    }

    protected static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, ResultActions performResult) throws Exception {
        OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiJson(expectedJsonFile, () -> getResponseBodyAsString(performResult));
    }

    protected static void assertResponseBodyMatchesOpenApiYaml(String expectedYamlFile, ResultActions performResult) throws Exception {
        OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiYaml(expectedYamlFile, () -> getResponseBodyAsString(performResult));
    }

    protected static String getResponseBodyAsString(ResultActions performResult) throws Exception {
        MvcResult mvcResult = performResult
                .andExpect(status().isOk())
                .andReturn();
        return mvcResult.getResponse().getContentAsString();
    }
}

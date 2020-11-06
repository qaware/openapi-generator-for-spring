package de.qaware.openapigeneratorforspring.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Autowired
    protected MockMvc mockMvc;

    protected static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, ResultActions performResult) throws Exception {
        OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiJson(expectedJsonFile, () -> getResponseBodyAsString(performResult));
    }

    protected static String getResponseBodyAsString(ResultActions performResult) throws Exception {
        MvcResult mvcResult = performResult
                .andExpect(status().isOk())
                .andReturn();
        return mvcResult.getResponse().getContentAsString();
    }
}

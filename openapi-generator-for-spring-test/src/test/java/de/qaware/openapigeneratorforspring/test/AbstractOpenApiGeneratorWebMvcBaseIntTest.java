package de.qaware.openapigeneratorforspring.test;

import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static de.qaware.openapigeneratorforspring.test.OpenApiJsonFileLoader.readOpenApiJsonFile;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Autowired
    protected MockMvc mockMvc;

    public static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, ResultActions performResult) throws Exception {
        String expectedJson = readOpenApiJsonFile(expectedJsonFile);
        MvcResult mvcResult = performResult
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = mvcResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }
}

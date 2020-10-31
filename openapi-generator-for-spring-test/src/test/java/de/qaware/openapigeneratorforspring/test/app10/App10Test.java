package de.qaware.openapigeneratorforspring.test.app10;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcIntTest.assertResponseBodyMatchesOpenApiJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class App10Test {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testWithQueryParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app10_admin.json",
                mockMvc.perform(get("/v3/api-docs").queryParam("pathPrefix", "/admin"))
        );
    }

    @Test
    public void testWithHeaderParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app10_user.json",
                mockMvc.perform(get("/v3/api-docs").header("X-Path-Prefix", "/user"))
        );
    }
}

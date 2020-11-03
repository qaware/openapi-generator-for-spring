package de.qaware.openapigeneratorforspring.test.app10;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class App10Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

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

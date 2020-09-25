package de.qaware.openapigeneratorforspring.test.app10;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorIntTest.assertResponseBodyMatchesOpenApiJson;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
public class App10Test {

    @Autowired
    protected WebTestClient webTestClient;

    @Test
    public void testWithQueryParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app10_admin.json",
                webTestClient.get().uri("/v3/api-docs").attribute("pathPrefix", "/admin")
        );
    }

    @Test
    public void testWithHeaderParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app10_user.json",
                webTestClient.get().uri("/v3/api-docs").header("X-Path-Prefix", "/user")
        );
    }
}

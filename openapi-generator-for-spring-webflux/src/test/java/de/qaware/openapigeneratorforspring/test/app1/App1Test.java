package de.qaware.openapigeneratorforspring.test.app1;

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
@AutoConfigureWebTestClient(timeout = "40000000")
public class App1Test {

    @Autowired
    protected WebTestClient webTestClient;

    @Test
    public void testWithQueryParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app1_admin.json",
                webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v3/api-docs")
                        .queryParam("pathPrefix", "/admin")
                        .build()
                )
        );
    }

    @Test
    public void testWithHeaderParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app1_user.json",
                webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v3/api-docs")
                        .build()
                ).header("X-Path-Prefix", "/user")
        );
    }
}

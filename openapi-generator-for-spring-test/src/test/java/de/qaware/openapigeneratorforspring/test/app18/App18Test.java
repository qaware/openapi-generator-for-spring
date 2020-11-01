package de.qaware.openapigeneratorforspring.test.app18;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "openapi-generator-for-spring.add-default-server = false")
public class App18Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    public void testWithQueryParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app18_admin.json",
                webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v3/api-docs")
                        .queryParam("pathPrefix", "/admin")
                        .build()
                )
        );
    }

    @Test
    public void testWithHeaderParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app18_user.json",
                webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/v3/api-docs")
                        .build()
                ).header("X-Path-Prefix", "/user")
        );
    }
}

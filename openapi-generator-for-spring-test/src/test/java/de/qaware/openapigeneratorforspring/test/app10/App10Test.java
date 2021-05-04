package de.qaware.openapigeneratorforspring.test.app10;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.jupiter.api.Test;

class App10Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Test
    void testWithQueryParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app10_admin.json",
                performApiDocsRequest(x -> x.queryParam("pathPrefix", "/admin"))
        );
    }

    @Test
    void testWithHeaderParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app10_user.json",
                performApiDocsRequest(x -> x.header("X-Path-Prefix", "/user"))
        );
    }
}

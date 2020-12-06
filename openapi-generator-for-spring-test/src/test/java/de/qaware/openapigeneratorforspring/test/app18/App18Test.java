package de.qaware.openapigeneratorforspring.test.app18;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;

public class App18Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    public void testWithQueryParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app18_admin.json",
                performApiDocsRequest(x -> x.queryParam("pathPrefix", "/admin"), x -> x)
        );
    }

    @Test
    public void testWithHeaderParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app18_user.json",
                performApiDocsRequest(x -> x, x -> x.header("X-Path-Prefix", "/user"))
        );
    }
}

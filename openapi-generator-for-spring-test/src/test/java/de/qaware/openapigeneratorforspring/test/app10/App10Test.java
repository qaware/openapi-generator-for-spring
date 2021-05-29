package de.qaware.openapigeneratorforspring.test.app10;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.jupiter.api.Test;

import static de.qaware.openapigeneratorforspring.test.OpenApiJsonIntegrationTestUtils.findResourceFilenameFromTestClass;

class App10Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Test
    void testWithQueryParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson(findResourceFilenameFromTestClass(getClass(), "_admin.json"),
                performApiDocsRequest(x -> x.queryParam("pathPrefix", "/admin"))
        );
    }

    @Test
    void testWithHeaderParam() throws Exception {
        assertResponseBodyMatchesOpenApiJson(findResourceFilenameFromTestClass(getClass(), "_user.json"),
                performApiDocsRequest(x -> x.header("X-Path-Prefix", "/user"))
        );
    }
}

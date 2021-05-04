package de.qaware.openapigeneratorforspring.test.app38;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class App38Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiYaml("app38.yaml",
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("application", "yaml")))
        );
    }
}

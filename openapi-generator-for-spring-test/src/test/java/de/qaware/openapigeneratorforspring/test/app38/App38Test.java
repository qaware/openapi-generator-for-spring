package de.qaware.openapigeneratorforspring.test.app38;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;
import org.springframework.http.MediaType;

public class App38Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiYaml("app38.yaml",
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("application", "yaml")))
        );
    }
}

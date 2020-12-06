package de.qaware.openapigeneratorforspring.test.app36;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.Test;

public class App36Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiYaml("app36.yaml",
                performApiDocsRequest(x -> x.accept("application/yaml"))
        );
    }
}

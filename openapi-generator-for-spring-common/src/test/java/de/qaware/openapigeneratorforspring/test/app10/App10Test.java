package de.qaware.openapigeneratorforspring.test.app10;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorIntTest.assertResponseBodyMatchesOpenApiJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Import(App10Test.TestConfig.class)
public class App10Test {

    @Autowired
    protected MockMvc mockMvc;

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

    @TestConfiguration
    static class TestConfig {

        // TODO Remove this once WebMVC is also separated for int tests
        @Bean
        public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(RequestMappingHandlerMapping requestMappingHandlerMapping) {
            return () -> requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                    .map(entry -> new HandlerMethodWithInfo(
                            entry.getValue(),
                            entry.getKey().getPatternsCondition().getPatterns(),
                            entry.getKey().getMethodsCondition().getMethods()
                    ))
                    .collect(Collectors.toList());
        }
    }
}

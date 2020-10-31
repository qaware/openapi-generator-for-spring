package de.qaware.openapigeneratorforspring.test.app9;

import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferenceIdentifierFactoryForRequestBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class App9Configuration {
    @Bean
    public ReferenceIdentifierFactoryForRequestBody referenceIdentifierFactoryForRequestBody() {
        return (item, suggestedIdentifier, numberOfSetters) -> "RequestBody";
    }
}

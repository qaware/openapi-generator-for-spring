package de.qaware.openapigeneratorforspring.test.app9;

import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferenceIdentifierBuilderForRequestBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class App9Configuration {
    @Bean
    public ReferenceIdentifierBuilderForRequestBody referenceIdentifierFactoryForRequestBody() {
        return (item, suggestedIdentifier, numberOfSetters) -> "RequestBody";
    }
}

package de.qaware.openapigeneratorforspring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.DefaultJava8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;

@EnableConfigurationProperties(Java8TimeTypeResolverConfigurationProperties.class)
public class OpenApiGeneratorSchemaTypeExtensionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Java8TimeInitialSchemaBuilder defaultJava8TimeInitialSchemaFactory(
            Java8TimeTypeResolverConfigurationProperties properties,
            // Use Spring Web's object mapper bean
            @Nullable ObjectMapper objectMapper
    ) {
        return new DefaultJava8TimeInitialSchemaBuilder(properties, objectMapper);
    }
}

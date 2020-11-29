package de.qaware.openapigeneratorforspring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForValidation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.DefaultJava8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;

@EnableConfigurationProperties(Java8TimeTypeResolverConfigurationProperties.class)
public class OpenApiGeneratorSchemaExtensionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Java8TimeInitialSchemaBuilder defaultJava8TimeInitialSchemaBuilder(
            Java8TimeTypeResolverConfigurationProperties properties,
            // Use Spring Web's object mapper bean
            @Nullable ObjectMapper objectMapper
    ) {
        return new DefaultJava8TimeInitialSchemaBuilder(properties, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Min.class)
    public SchemaCustomizerForValidation defaultSchemaCustomizerForValidation() {
        return new SchemaCustomizerForValidation();
    }
}

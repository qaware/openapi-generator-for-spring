package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.DefaultJava8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeInitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.spring.DefaultSpringResponseEntityTypeResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(Java8TimeTypeResolverConfigurationProperties.class)
public class OpenApiGeneratorSchemaTypeExtensionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Java8TimeInitialSchemaBuilder defaultJava8TimeInitialSchemaFactory(Java8TimeTypeResolverConfigurationProperties properties) {
        return new DefaultJava8TimeInitialSchemaBuilder(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultSpringResponseEntityTypeResolver defaultDefaultSpringResponseEntityTypeResolver() {
        return new DefaultSpringResponseEntityTypeResolver();
    }
}

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.util.DefaultOpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.util.DefaultOpenApiSpringBootApplicationClassSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationClassSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorUtilAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public OpenApiObjectMapperSupplier defaultOpenApiObjectMapperSupplier() {
        return new DefaultOpenApiObjectMapperSupplier();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSpringBootApplicationClassSupplier defaultOpenApiSpringBootApplicationClassSupplier() {
        return new DefaultOpenApiSpringBootApplicationClassSupplier();
    }
}

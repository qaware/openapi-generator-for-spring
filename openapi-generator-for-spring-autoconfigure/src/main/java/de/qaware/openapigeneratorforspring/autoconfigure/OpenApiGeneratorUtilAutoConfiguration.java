package de.qaware.openapigeneratorforspring.autoconfigure;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import io.swagger.v3.core.util.Json;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorUtilAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public OpenApiObjectMapperSupplier defaultOpenApiObjectMapperSupplier() {
        // use swagger-core's object mapper by default
        // TODO consider this choice again: Maybe the "auto-configured" object
        //  mapper from spring would work better?
        return () -> Json.mapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }
}

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiSpringBootApplicationClassSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationClassSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiLoggingUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.beans.factory.InitializingBean;
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

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSpringBootApplicationAnnotationsSupplier defaultOpenApiSpringBootApplicationAnnotationsSupplier(
            OpenApiSpringBootApplicationClassSupplier springBootApplicationClassSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOpenApiSpringBootApplicationAnnotationsSupplier(springBootApplicationClassSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAPIDefinitionAnnotationSupplier defaultOpenAPIDefinitionAnnotationSupplier(
            OpenApiSpringBootApplicationAnnotationsSupplier openApiSpringBootApplicationAnnotationsSupplier
    ) {
        return new DefaultOpenAPIDefinitionAnnotationSupplier(openApiSpringBootApplicationAnnotationsSupplier);
    }

    public interface OpenApiLoggingUtilsRegistrationBean extends InitializingBean {

    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiLoggingUtilsRegistrationBean defaultOpenApiLoggingUtilsRegistrationBean() {
        return () -> OpenApiLoggingUtils.registerPrettyPrinter(Schema.class, OpenApiLoggingUtils::prettyPrintSchema);
    }

}

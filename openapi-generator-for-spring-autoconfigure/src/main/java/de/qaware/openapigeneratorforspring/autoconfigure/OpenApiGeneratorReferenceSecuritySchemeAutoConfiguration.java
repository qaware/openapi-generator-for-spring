package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.component.securityscheme.ReferencedSecuritySchemesHandlerFactory;
import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiGeneratorReferenceSecuritySchemeAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReferencedSecuritySchemesHandlerFactory referencedSecuritySchemesHandlerFactory(
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier,
            SecuritySchemeAnnotationMapper openApiSecuritySchemesSuppliers,
            List<OpenApiSecuritySchemesSupplier> securitySchemeAnnotationMappers
    ) {
        return new ReferencedSecuritySchemesHandlerFactory(
                springBootApplicationAnnotationsSupplier, openApiSecuritySchemesSuppliers, securitySchemeAnnotationMappers
        );
    }
}

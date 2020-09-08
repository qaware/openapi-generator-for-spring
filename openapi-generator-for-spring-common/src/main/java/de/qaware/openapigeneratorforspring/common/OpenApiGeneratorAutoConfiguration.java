package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        OpenApiGeneratorReferenceAutoConfiguration.class,
        OpenApiGeneratorPathsAutoConfiguration.class,
        OpenApiGeneratorUtilAutoConfiguration.class
})
public class OpenApiGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResource openApiResource(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier openApiObjectMapperSupplier) {
        return new OpenApiResource(openApiGenerator, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiGenerator openApiGenerator(
            PathsBuilder pathsBuilder,
            ReferenceNameFactory referenceNameFactory,
            ReferenceNameConflictResolver referenceNameConflictResolver
    ) {
        return new OpenApiGenerator(
                pathsBuilder,
                referenceNameFactory,
                referenceNameConflictResolver
        );
    }
}

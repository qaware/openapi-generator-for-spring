package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceDeciderForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceNameConflictResolverForApiResponse;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceNameConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        OpenApiGeneratorReferenceAutoConfiguration.class,
        OpenApiGeneratorPathsAutoConfiguration.class,
        OpenApiGeneratorUtilAutoConfiguration.class,
        OpenApiGeneratorInfoAutoConfiguration.class
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
            OpenApiInfoSupplier openApiInfoSupplier,
            ReferenceNameFactory referenceNameFactory,
            ReferenceNameConflictResolverForSchema referenceNameConflictResolverForSchema,
            ReferenceDeciderForSchema referenceDeciderForSchema,
            ReferenceNameConflictResolverForApiResponse referenceNameConflictResolverForApiResponse,
            ReferenceDeciderForApiResponse referenceDeciderForApiResponse
    ) {
        return new OpenApiGenerator(
                pathsBuilder,
                openApiInfoSupplier,
                referenceNameFactory,
                referenceNameConflictResolverForSchema,
                referenceDeciderForSchema,
                referenceNameConflictResolverForApiResponse,
                referenceDeciderForApiResponse
        );
    }
}

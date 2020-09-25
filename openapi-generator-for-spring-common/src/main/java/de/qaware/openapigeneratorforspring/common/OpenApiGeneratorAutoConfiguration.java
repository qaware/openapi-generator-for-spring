package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            ReferencedItemSupportFactory referencedItemSupportFactory,
            Optional<List<OpenApiCustomizer>> optionalOpenApiCustomizers
    ) {
        return new OpenApiGenerator(
                pathsBuilder,
                openApiInfoSupplier,
                referencedItemSupportFactory,
                optionalOpenApiCustomizers.orElse(Collections.emptyList())
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResourceParameterProvider defaultOpenApiResourceParameterProvider(HttpServletRequest httpServletRequest) {
        return new DefaultOpenApiResourceParameterProvider(httpServletRequest);
    }
}

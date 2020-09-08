package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.filter.operation.ExcludeHiddenOperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.NoOperationsPathItemFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
        OpenApiGeneratorReferenceAutoConfiguration.class
})
public class OpenApiGeneratorFilterAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public NoOperationsPathItemFilter noOperationsPathItemFilter() {
        return new NoOperationsPathItemFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcludeHiddenOperationFilter excludeHiddenOperationFilter(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new ExcludeHiddenOperationFilter(annotationsSupplierFactory);
    }
}

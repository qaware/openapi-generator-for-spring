package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.filter.handlermethod.ExcludeHiddenHandlerMethodFilter;
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
    public ExcludeHiddenHandlerMethodFilter excludeHiddenHandlerMethodFilter(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new ExcludeHiddenHandlerMethodFilter(annotationsSupplierFactory);
    }
}

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.annotation.DefaultAnnotationsSupplierFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorAnnotationAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AnnotationsSupplierFactory defaultAnnotationsSupplierFactory() {
        return new DefaultAnnotationsSupplierFactory();
    }
}

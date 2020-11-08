package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.util.OpenApiLoggingUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorUtilAutoConfiguration {

    public interface OpenApiLoggingUtilsRegistrationBean extends InitializingBean {

    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiLoggingUtilsRegistrationBean defaultOpenApiLoggingUtilsRegistrationBean() {
        return () -> OpenApiLoggingUtils.registerPrettyPrinter(Schema.class, OpenApiLoggingUtils::prettyPrintSchema);
    }

}

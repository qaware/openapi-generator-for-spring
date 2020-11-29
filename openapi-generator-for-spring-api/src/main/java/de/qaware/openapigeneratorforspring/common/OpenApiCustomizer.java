package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.model.OpenApi;
import org.springframework.core.Ordered;

/**
 * Customizer for {@link OpenApi}. Most general customizer and
 * is run after the Open Api model has been completely built.
 */
@FunctionalInterface
public interface OpenApiCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    void customize(OpenApi openApi);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}

package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.model.OpenApi;
import org.springframework.core.Ordered;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
@FunctionalInterface
public interface OpenApiCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    void customize(OpenApi openApi);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}

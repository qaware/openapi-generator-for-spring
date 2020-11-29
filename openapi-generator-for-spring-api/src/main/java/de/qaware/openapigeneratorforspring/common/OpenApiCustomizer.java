package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.OpenApi;

/**
 * Customizer for {@link OpenApi}. Most general customizer and is
 * run after the Open Api model has been completely built (including
 * application of default implementations of this interface).
 */
@FunctionalInterface
public interface OpenApiCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize the given Open Api by reference.
     *
     * @param openApi open api model
     */
    void customize(OpenApi openApi);
}

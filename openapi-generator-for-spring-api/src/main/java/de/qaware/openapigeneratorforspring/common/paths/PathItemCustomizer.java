package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import org.springframework.core.Ordered;

/**
 * Customizer for {@link PathItem path item} of the
 * OpenApi model. Is run AFTER the path item is built.
 */
@FunctionalInterface
public interface PathItemCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    /**
     * Customize the given path item by reference.
     *
     * @param pathItem                       path item
     * @param pathPattern                    path pattern for this path item
     * @param referencedItemConsumerSupplier for referencing additional items or adapt the referencing
     */
    void customize(PathItem pathItem, String pathPattern, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}

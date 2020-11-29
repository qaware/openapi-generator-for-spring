package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.path.PathItem;

/**
 * Customizer for {@link PathItem path item} of the
 * OpenApi model. Is run AFTER the path item is built.
 */
@FunctionalInterface
public interface PathItemCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize the given path item by reference.
     *
     * @param pathItem                       path item
     * @param pathPattern                    path pattern for this path item
     * @param referencedItemConsumerSupplier for referencing additional items or adapt the referencing
     */
    void customize(PathItem pathItem, String pathPattern, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}

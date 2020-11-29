package de.qaware.openapigeneratorforspring.common.filter.pathitem;

import de.qaware.openapigeneratorforspring.model.path.PathItem;

/**
 * Filter for path items. Run AFTER the {@link PathItem path item} is built.
 *
 * <p>This filter does not remove possibly referenced
 * items due to operation building. Prefer using {@link
 * de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter
 * HandlerMethodFilter} whenever possible.
 */
@FunctionalInterface
public interface PathItemFilter {
    /**
     * Accept the given path item to become part of the OpenApi model.
     *
     * @param pathItem    built path item
     * @param pathPattern path pattern this path item is built for
     * @return true if path item shall be included, false otherwise
     */
    boolean accept(PathItem pathItem, String pathPattern);
}

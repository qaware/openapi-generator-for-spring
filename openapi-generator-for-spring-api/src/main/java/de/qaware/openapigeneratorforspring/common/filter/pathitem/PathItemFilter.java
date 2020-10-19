package de.qaware.openapigeneratorforspring.common.filter.pathitem;

import de.qaware.openapigeneratorforspring.model.path.PathItem;

@FunctionalInterface
public interface PathItemFilter {
    boolean accept(PathItem pathItem, String pathPattern);
}

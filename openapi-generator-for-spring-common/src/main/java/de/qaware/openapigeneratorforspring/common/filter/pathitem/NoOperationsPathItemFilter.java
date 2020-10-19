package de.qaware.openapigeneratorforspring.common.filter.pathitem;

import de.qaware.openapigeneratorforspring.model.path.PathItem;

public class NoOperationsPathItemFilter implements PathItemFilter {
    @Override
    public boolean accept(PathItem pathItem, String pathPattern) {
        return !pathItem.getOperations().isEmpty();
    }
}

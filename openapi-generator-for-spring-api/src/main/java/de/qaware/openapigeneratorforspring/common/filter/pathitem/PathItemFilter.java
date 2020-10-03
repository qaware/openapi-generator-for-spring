package de.qaware.openapigeneratorforspring.common.filter.pathitem;

import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public interface PathItemFilter {

    default boolean accept(PathItem pathItem) {
        // prevent fall-through
        throw new IllegalStateException("Need to override at least one method of this interface");
    }

    default boolean accept(PathItem pathItem, String pathPattern) {
        return accept(pathItem);
    }

    default boolean accept(PathItem pathItem, String pathPattern, Map<RequestMethod, Operation> operationPerMethod) {
        return accept(pathItem, pathPattern);
    }
}
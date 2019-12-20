package de.qaware.openapigeneratorforspring.common.filter.pathitem;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public interface PathItemFilter {

    default boolean accept(PathItem pathItem) {
        throw new NotImplementedException("Need to override at least one method");
    }

    default boolean accept(PathItem pathItem, String pathPattern) {
        return accept(pathItem);
    }

    default boolean accept(PathItem pathItem, String pathPattern, Map<RequestMethod, Operation> operationPerMethod) {
        return accept(pathItem, pathPattern);
    }
}

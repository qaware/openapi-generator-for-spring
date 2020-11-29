package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.path.RequestMethod;
import lombok.Value;

/**
 * Container class for operation info.
 */
@Value(staticConstructor = "of")
public class OperationInfo {
    /**
     * Handler method to be used for operation building.
     */
    HandlerMethod handlerMethod;
    /**
     * Request method (GET, POST, ...)
     */
    RequestMethod requestMethod;
    /**
     * Path pattern used for this operation.
     */
    String pathPattern;
}

package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.path.RequestMethod;
import lombok.Value;

@Value(staticConstructor = "of")
public class OperationInfo {
    HandlerMethod handlerMethod;
    RequestMethod requestMethod;
    String pathPattern;
}

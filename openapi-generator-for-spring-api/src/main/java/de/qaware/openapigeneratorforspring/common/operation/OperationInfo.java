package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestMethod;

@Value(staticConstructor = "of")
public class OperationInfo {
    HandlerMethod handlerMethod;
    RequestMethod requestMethod;
    String pathPattern;
}

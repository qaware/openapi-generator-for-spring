package de.qaware.openapigeneratorforspring.common.operation;

import lombok.Value;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

@Value(staticConstructor = "of")
public class OperationInfo {
    HandlerMethod handlerMethod;
    RequestMethod requestMethod;
    String pathPattern;
}

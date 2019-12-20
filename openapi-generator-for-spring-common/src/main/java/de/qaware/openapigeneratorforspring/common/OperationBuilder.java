package de.qaware.openapigeneratorforspring.common;

import io.swagger.v3.oas.models.Operation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

public class OperationBuilder {
    public Operation buildOperation(RequestMethod requestMethod, String pathPattern, HandlerMethod handlerMethod) {
        return new Operation().description(handlerMethod.getShortLogMessage());
    }
}

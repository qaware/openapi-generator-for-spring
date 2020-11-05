package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.model.path.RequestMethod;
import lombok.Value;

import java.util.Set;

@Value
public class HandlerMethodWithInfo {
    HandlerMethod handlerMethod;
    Set<String> pathPatterns;
    Set<RequestMethod> requestMethods;
}

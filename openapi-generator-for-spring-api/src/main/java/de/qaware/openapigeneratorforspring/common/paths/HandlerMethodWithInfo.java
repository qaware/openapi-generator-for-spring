package de.qaware.openapigeneratorforspring.common.paths;

import lombok.Value;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@Value
public class HandlerMethodWithInfo {
    HandlerMethod handlerMethod;
    Set<String> pathPatterns;
    Set<RequestMethod> requestMethods;
}

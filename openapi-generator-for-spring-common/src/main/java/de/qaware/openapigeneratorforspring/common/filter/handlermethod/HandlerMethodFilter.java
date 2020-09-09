package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import org.springframework.web.method.HandlerMethod;

public interface HandlerMethodFilter {
    boolean accept(HandlerMethod handlerMethod);
}

package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;

public interface HandlerMethodFilter {
    boolean accept(HandlerMethodWithInfo handlerMethodWithInfo);
}

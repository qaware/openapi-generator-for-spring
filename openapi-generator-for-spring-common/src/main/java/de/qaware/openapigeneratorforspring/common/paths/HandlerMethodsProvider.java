package de.qaware.openapigeneratorforspring.common.paths;

import java.util.List;

@FunctionalInterface
public interface HandlerMethodsProvider {
    List<HandlerMethodWithInfo> getHandlerMethods();
}

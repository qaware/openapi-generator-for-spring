package de.qaware.openapigeneratorforspring.common.paths;

import java.util.List;

/**
 * Provider for list of {@link HandlerMethod handler
 * methods}. Implementations search for handler methods
 * in the application context and typically build them.
 */
@FunctionalInterface
public interface HandlerMethodsProvider {
    /**
     * Provide handler methods.
     *
     * @return list of handler methods
     */
    List<HandlerMethodWithInfo> getHandlerMethods();
}

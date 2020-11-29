package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;

/**
 * Filter for discovered {@link de.qaware.openapigeneratorforspring.common.paths.HandlerMethod HandlerMethods}.
 * Is evaluated before path items are constructed.
 */
@FunctionalInterface
public interface HandlerMethodFilter {
    /**
     * Accept the given handler method.
     *
     * @param handlerMethodWithInfo handler method including more information about it
     * @return true if it shall be processed, false otherwise
     */
    boolean accept(HandlerMethodWithInfo handlerMethodWithInfo);
}

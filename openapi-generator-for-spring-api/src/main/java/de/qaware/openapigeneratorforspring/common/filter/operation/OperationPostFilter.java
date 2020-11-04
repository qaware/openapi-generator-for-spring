package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.operation.Operation;

@FunctionalInterface
public interface OperationPostFilter {
    boolean postAccept(Operation operation, HandlerMethod handlerMethod);
}

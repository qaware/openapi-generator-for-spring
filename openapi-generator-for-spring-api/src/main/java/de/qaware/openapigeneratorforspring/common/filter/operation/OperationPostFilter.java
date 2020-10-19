package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.model.operation.Operation;
import org.springframework.web.method.HandlerMethod;

@FunctionalInterface
public interface OperationPostFilter {
    boolean postAccept(Operation operation, HandlerMethod handlerMethod);
}

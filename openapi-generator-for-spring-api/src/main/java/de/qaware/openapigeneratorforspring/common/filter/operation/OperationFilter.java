package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.model.operation.Operation;
import org.springframework.web.method.HandlerMethod;

public interface OperationFilter {
    boolean accept(Operation operation, HandlerMethod handlerMethod);
}

package de.qaware.openapigeneratorforspring.common.filter.operation;

import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.method.HandlerMethod;

public interface OperationFilter {

    default boolean accept(Operation operation) {
        throw new NotImplementedException("Need to override at least one method");
    }

    default boolean accept(Operation operation, HandlerMethod handlerMethod) {
        return accept(operation);
    }
}

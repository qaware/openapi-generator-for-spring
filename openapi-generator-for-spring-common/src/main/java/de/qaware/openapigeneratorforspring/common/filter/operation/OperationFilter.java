package de.qaware.openapigeneratorforspring.common.filter.operation;

import io.swagger.v3.oas.models.Operation;
import org.springframework.web.method.HandlerMethod;

public interface OperationFilter {
    boolean accept(Operation operation, HandlerMethod handlerMethod);
}

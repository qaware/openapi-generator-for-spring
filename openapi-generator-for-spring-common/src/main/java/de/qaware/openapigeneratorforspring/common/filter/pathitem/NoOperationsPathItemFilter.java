package de.qaware.openapigeneratorforspring.common.filter.pathitem;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public class NoOperationsPathItemFilter implements PathItemFilter {
    @Override
    public boolean accept(PathItem pathItem, String pathPattern, Map<RequestMethod, Operation> operationPerMethod) {
        return !operationPerMethod.isEmpty();
    }
}

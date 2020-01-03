package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.schema.NestedSchemaConsumer;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

@Value
public class OperationBuilderContext {
    RequestMethod requestMethod;
    String pathPattern;
    HandlerMethod handlerMethod;
    NestedSchemaConsumer nestedSchemaConsumer;
}

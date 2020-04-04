package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import lombok.Value;

@Value
public class OperationBuilderContext {
    OperationInfo operationInfo;
    ReferencedSchemaConsumer referencedSchemaConsumer;
}

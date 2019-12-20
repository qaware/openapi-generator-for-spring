package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import lombok.Value;

@Value(staticConstructor = "of")
public class OperationIdConflict {
    Operation operation;
    OperationBuilderContext context;
}

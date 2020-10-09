package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.tag.Tag;

import java.util.List;
import java.util.function.Consumer;

public interface OperationAnnotationMapper {
    Operation map(io.swagger.v3.oas.annotations.Operation operationAnnotation,
                  ReferencedItemConsumerSupplier referencedItemConsumerSupplier, Consumer<List<Tag>> tagsConsumer);
}

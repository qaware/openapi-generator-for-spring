package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public class OperationBuilderContext {
    private final OperationInfo operationInfo;
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;
    private final Consumer<List<Tag>> tagsConsumer;
}

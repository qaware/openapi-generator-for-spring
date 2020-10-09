package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Callback;
import de.qaware.openapigeneratorforspring.model.tag.Tag;

import java.util.List;
import java.util.function.Consumer;

public interface CallbackAnnotationMapper {
    Callback map(io.swagger.v3.oas.annotations.callbacks.Callback callbackAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier, Consumer<List<Tag>> tagsConsumer);
}

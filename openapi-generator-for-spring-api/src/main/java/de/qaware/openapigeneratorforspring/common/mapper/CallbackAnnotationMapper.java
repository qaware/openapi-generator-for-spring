package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Callback;

public interface CallbackAnnotationMapper {
    Callback map(io.swagger.v3.oas.annotations.callbacks.Callback callbackAnnotation,
                 ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}
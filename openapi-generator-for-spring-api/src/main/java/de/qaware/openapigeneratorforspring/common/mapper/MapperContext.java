package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public interface MapperContext {
    <T extends ReferencedItemConsumer> T getReferenceConsumer(Class<T> referencedItemConsumerClazz);

    MapperContext withReferenceOwner(@Nullable Object owner);

    List<String> getSuggestedMediaTypes(Class<? extends HasContent> owningType);

    MapperContext withSuggestedMediaTypesSupplierFor(Class<? extends HasContent> owningType, Supplier<List<String>> suggestedMediaTypesSupplier);
}

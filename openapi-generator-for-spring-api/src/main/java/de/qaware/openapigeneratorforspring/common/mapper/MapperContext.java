package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public interface MapperContext extends HasReferencedItemConsumer {

    MapperContext withReferencedItemOwner(@Nullable Object owner);

    List<String> getSuggestedMediaTypes(Class<? extends HasContent> owningType);

    MapperContext withSuggestedMediaTypesSupplierFor(Class<? extends HasContent> owningType, Supplier<List<String>> suggestedMediaTypesSupplier);
}

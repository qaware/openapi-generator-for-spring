package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;

import javax.annotation.Nullable;
import java.util.List;

public interface MapperContext {
    <T extends ReferencedItemConsumer> T getReferenceConsumer(Class<T> referencedItemConsumerClazz);
    MapperContext withReferenceOwner(@Nullable Object owner);

    List<String> getSuggestedMediaTypes();
    MapperContext withSuggestedMediaTypes(List<String> suggestedMediaTypes);
}

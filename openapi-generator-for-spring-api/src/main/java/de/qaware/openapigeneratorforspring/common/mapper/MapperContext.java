package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public interface MapperContext extends HasReferencedItemConsumer {

    MapperContext withReferencedItemOwner(@Nullable Object owner);

    Optional<Set<String>> findMediaTypes(Class<? extends HasContent> owningType);

    MapperContext withMediaTypesProvider(MediaTypesProvider mediaTypesProvider);
}

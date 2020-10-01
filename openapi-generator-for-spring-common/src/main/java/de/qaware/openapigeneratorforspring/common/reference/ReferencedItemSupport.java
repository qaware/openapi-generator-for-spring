package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.model.Components;

import javax.annotation.Nullable;

public interface ReferencedItemSupport {
    ReferencedItemConsumerSupplier getReferencedItemConsumerSupplier();

    @Nullable
    Components buildComponents();
}

package de.qaware.openapigeneratorforspring.common.reference;

import io.swagger.v3.oas.models.Components;

import javax.annotation.Nullable;

public interface ReferencedItemSupport {
    ReferencedItemConsumerSupplier getReferencedItemConsumerSupplier();

    @Nullable
    Components buildComponents();
}

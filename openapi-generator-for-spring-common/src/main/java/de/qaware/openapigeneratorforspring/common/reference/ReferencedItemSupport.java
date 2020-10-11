package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.model.Components;

public interface ReferencedItemSupport {
    ReferencedItemConsumerSupplier getReferencedItemConsumerSupplier();

    Components buildComponents();
}

package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.model.OpenApi;

public interface ReferencedItemSupport {
    ReferencedItemConsumerSupplier getReferencedItemConsumerSupplier();

    void applyToOpenApi(OpenApi openApi);
}

package de.qaware.openapigeneratorforspring.common.reference.handler;


import de.qaware.openapigeneratorforspring.model.OpenApi;

import javax.annotation.Nullable;

public interface ReferencedItemHandler {
    void applyToOpenApi(OpenApi openApi);

    default ReferencedItemHandler withOwner(@Nullable Object object) {
        return this;
    }
}

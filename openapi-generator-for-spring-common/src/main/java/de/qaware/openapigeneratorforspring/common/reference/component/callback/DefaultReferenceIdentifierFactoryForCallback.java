package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.model.operation.Callback;

import javax.annotation.Nullable;

public class DefaultReferenceIdentifierFactoryForCallback implements ReferenceIdentifierFactoryForCallback {

    @Override
    @Nullable
    public String buildIdentifier(Callback item, @Nullable String suggestedIdentifier, int numberOfSetters) {
        return suggestedIdentifier;
    }
}

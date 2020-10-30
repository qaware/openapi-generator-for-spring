package de.qaware.openapigeneratorforspring.common.reference.component.requestbody;

import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;

import javax.annotation.Nullable;

public class DefaultReferenceIdentifierFactoryForRequestBody implements ReferenceIdentifierFactoryForRequestBody {
    @Override
    @Nullable
    public String buildIdentifier(RequestBody item, @Nullable String suggestedIdentifier, int numberOfSetters) {
        return suggestedIdentifier;
    }
}

package de.qaware.openapigeneratorforspring.common.reference.component.response;

import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

import javax.annotation.Nullable;

public class DefaultReferenceIdentifierFactoryForApiResponse implements ReferenceIdentifierFactoryForApiResponse {

    @Override
    @Nullable
    public String buildIdentifier(ApiResponse item, @Nullable String suggestedIdentifier, int numberOfSetters) {
        return suggestedIdentifier;
    }
}

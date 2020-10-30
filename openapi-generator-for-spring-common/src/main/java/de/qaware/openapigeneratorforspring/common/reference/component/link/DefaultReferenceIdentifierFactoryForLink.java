package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.model.link.Link;

import javax.annotation.Nullable;

public class DefaultReferenceIdentifierFactoryForLink implements ReferenceIdentifierFactoryForLink {

    @Override
    @Nullable
    public String buildIdentifier(Link item, @Nullable String suggestedIdentifier, int numberOfSetters) {
        return suggestedIdentifier;
    }
}

package de.qaware.openapigeneratorforspring.common.reference.component.parameter;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;

public class DefaultReferenceIdentifierFactoryForParameter implements ReferenceIdentifierFactoryForParameter {
    @Override
    @Nullable
    public String buildIdentifier(Parameter item, @Nullable String suggestedIdentifier, int numberOfSetters) {
        return suggestedIdentifier;
    }
}

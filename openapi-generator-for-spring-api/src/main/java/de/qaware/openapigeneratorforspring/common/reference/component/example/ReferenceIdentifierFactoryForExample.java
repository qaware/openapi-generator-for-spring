package de.qaware.openapigeneratorforspring.common.reference.component.example;

import javax.annotation.Nullable;

public interface ReferenceIdentifierFactoryForExample {
    String buildSuggestedIdentifier(@Nullable String exampleName);
}

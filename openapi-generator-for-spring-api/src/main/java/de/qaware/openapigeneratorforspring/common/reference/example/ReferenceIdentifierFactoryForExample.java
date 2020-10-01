package de.qaware.openapigeneratorforspring.common.reference.example;

import javax.annotation.Nullable;

public interface ReferenceIdentifierFactoryForExample {
    String buildSuggestedIdentifier(@Nullable String exampleName);
}

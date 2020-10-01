package de.qaware.openapigeneratorforspring.common.reference.header;

import javax.annotation.Nullable;

public interface ReferenceIdentifierFactoryForHeader {
    String buildSuggestedIdentifier(@Nullable String headerName);
}

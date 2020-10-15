package de.qaware.openapigeneratorforspring.common.reference.component.header;

import javax.annotation.Nullable;

public interface ReferenceIdentifierFactoryForHeader {
    String buildSuggestedIdentifier(@Nullable String headerName);
}

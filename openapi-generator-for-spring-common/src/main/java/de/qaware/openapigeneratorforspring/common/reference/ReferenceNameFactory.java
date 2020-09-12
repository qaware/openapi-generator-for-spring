package de.qaware.openapigeneratorforspring.common.reference;

import javax.annotation.Nullable;

public interface ReferenceNameFactory {
    ReferenceName create(Object object, @Nullable String suggestedIdentifier);
}

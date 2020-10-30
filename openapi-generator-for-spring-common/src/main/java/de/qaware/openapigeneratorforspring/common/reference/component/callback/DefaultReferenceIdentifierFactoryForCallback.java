package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.model.operation.Callback;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class DefaultReferenceIdentifierFactoryForCallback implements ReferenceIdentifierFactoryForCallback {

    @Override
    public String buildIdentifier(Callback item, @Nullable String suggestedIdentifier) {
        if (StringUtils.isBlank(suggestedIdentifier)) {
            throw new IllegalStateException("A callback should always have a suggested identifier");
        }
        return suggestedIdentifier;
    }
}

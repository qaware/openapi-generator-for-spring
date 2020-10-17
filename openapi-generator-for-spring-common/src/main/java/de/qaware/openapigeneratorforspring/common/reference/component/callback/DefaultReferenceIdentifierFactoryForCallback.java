package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.model.operation.Callback;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class DefaultReferenceIdentifierFactoryForCallback implements ReferenceIdentifierFactoryForCallback {

    @Override
    public List<Object> buildIdentifierComponents(Callback item, @Nullable String suggestedIdentifier) {
        if (StringUtils.isBlank(suggestedIdentifier)) {
            throw new IllegalStateException("A callback should always have a suggested identifier");
        }
        return Collections.singletonList(suggestedIdentifier);
    }
}

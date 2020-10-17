package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.model.link.Link;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class DefaultReferenceIdentifierFactoryForLink implements ReferenceIdentifierFactoryForLink {

    @Override
    public List<Object> buildIdentifierComponents(Link item, @Nullable String suggestedIdentifier) {
        if (StringUtils.isBlank(suggestedIdentifier)) {
            throw new IllegalStateException("A link should always have a suggested identifier");
        }
        return Collections.singletonList(suggestedIdentifier);
    }
}

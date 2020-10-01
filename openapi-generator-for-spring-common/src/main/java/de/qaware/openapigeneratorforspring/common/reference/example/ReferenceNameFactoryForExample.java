package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import de.qaware.openapigeneratorforspring.model.example.Example;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public interface ReferenceNameFactoryForExample extends ReferenceNameFactoryForType<Example> {

    String buildSuggestedIdentifier(@Nullable String exampleName);

    @Override
    default List<Object> buildIdentifierComponents(Example item, @Nullable String suggestedIdentifier) {
        if (StringUtils.isBlank(suggestedIdentifier)) {
            throw new IllegalStateException("The reference name factory for example should always build a non-blank suggested identifier");
        }
        return Collections.singletonList(suggestedIdentifier);
    }
}

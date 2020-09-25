package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.headers.Header;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public interface ReferenceNameFactoryForHeader extends ReferenceNameFactoryForType<Header> {

    String buildSuggestedIdentifier(@Nullable String headerName);

    @Override
    default List<Object> buildIdentifierComponents(Header item, @Nullable String suggestedIdentifier) {
        if (StringUtils.isBlank(suggestedIdentifier)) {
            throw new IllegalStateException("The reference name factory for header should always build a non-blank suggested identifier");
        }
        return Collections.singletonList(suggestedIdentifier);
    }
}

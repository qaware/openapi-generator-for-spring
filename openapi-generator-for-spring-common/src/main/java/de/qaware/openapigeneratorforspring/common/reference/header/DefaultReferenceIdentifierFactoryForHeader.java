package de.qaware.openapigeneratorforspring.common.reference.header;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceIdentifierFactoryForHeader implements ReferenceIdentifierFactoryForHeader {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public String buildSuggestedIdentifier(@Nullable String headerName) {
        return StringUtils.isNotBlank(headerName) ? headerName
                : "Header" + counter.getAndIncrement();
    }
}

package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceNameFactoryForHeader implements ReferenceNameFactoryForHeader {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public ReferenceName.Type getReferenceNameType() {
        return ReferenceName.Type.HEADER;
    }

    @Override
    public String buildSuggestedIdentifier(@Nullable String headerName) {
        return StringUtils.isNotBlank(headerName) ? headerName
                : "Header" + counter.getAndIncrement();
    }
}

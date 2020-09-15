package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceNameFactoryForExample implements ReferenceNameFactoryForExample {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public ReferenceName.Type getReferenceNameType() {
        return ReferenceName.Type.EXAMPLE;
    }

    @Override
    public String buildSuggestedIdentifier(@Nullable String exampleName) {
        return StringUtils.isNotBlank(exampleName) ? exampleName
                : "Example" + counter.getAndIncrement();
    }
}

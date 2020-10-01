package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceIdentifierFactoryForParameter implements ReferenceIdentifierFactoryForParameter {
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public List<Object> buildIdentifierComponents(Parameter item, @Nullable String suggestedIdentifier) {
        return StringUtils.isNotBlank(suggestedIdentifier) ? Collections.singletonList(suggestedIdentifier)
                : Arrays.asList("Parameter", counter.getAndIncrement());
    }
}

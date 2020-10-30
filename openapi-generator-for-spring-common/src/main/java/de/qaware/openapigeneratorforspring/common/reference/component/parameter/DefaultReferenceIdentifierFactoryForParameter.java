package de.qaware.openapigeneratorforspring.common.reference.component.parameter;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceIdentifierFactoryForParameter implements ReferenceIdentifierFactoryForParameter {
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public String buildIdentifier(Parameter item, @Nullable String suggestedIdentifier) {
        return StringUtils.isNotBlank(suggestedIdentifier) ? suggestedIdentifier : "Parameter_" + counter.getAndIncrement();
    }
}

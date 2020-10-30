package de.qaware.openapigeneratorforspring.common.reference.component.requestbody;

import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceIdentifierFactoryForRequestBody implements ReferenceIdentifierFactoryForRequestBody {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public String buildIdentifier(RequestBody item, @Nullable String suggestedIdentifier) {
        return StringUtils.isNotBlank(suggestedIdentifier) ? suggestedIdentifier : "RequestBody_" + counter.getAndIncrement();
    }
}

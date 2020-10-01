package de.qaware.openapigeneratorforspring.common.reference.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceNameFactoryForRequestBody implements ReferenceNameFactoryForRequestBody {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public ReferenceName.Type getReferenceNameType() {
        return ReferenceName.Type.REQUEST_BODY;
    }

    @Override
    public List<Object> buildIdentifierComponents(RequestBody item, @Nullable String suggestedIdentifier) {
        return StringUtils.isNotBlank(suggestedIdentifier) ? Collections.singletonList(suggestedIdentifier) :
                Arrays.asList("RequestBody", counter.getAndIncrement());
    }
}

package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Boolean.TRUE;

public class DefaultReferenceNameFactoryForSchema implements ReferenceNameFactoryForSchema {

    private static final String NULLABLE_PREFIX = "nullable";
    private static final String DEPRECATED_PREFIX = "deprecated";


    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public ReferenceName.Type getReferenceNameType() {
        return ReferenceName.Type.SCHEMA;
    }

    @Override
    public List<Object> buildIdentifierComponents(Schema schema, @Nullable String suggestedIdentifier) {
        List<Object> identifierComponents = new ArrayList<>();
        if (StringUtils.isNotBlank(suggestedIdentifier)) {
            identifierComponents.add(suggestedIdentifier);
        } else if (StringUtils.isNotBlank(schema.getType())) {
            if (TRUE.equals(schema.getNullable())) {
                identifierComponents.add(NULLABLE_PREFIX);
            }
            if (TRUE.equals(schema.getDeprecated())) {
                identifierComponents.add(DEPRECATED_PREFIX);
            }
            identifierComponents.add(schema.getType());
            if (StringUtils.isNotBlank(schema.getFormat())) {
                identifierComponents.add(schema.getFormat());
            }
        } else {
            identifierComponents.addAll(Arrays.asList("Schema", counter.getAndIncrement()));
        }
        return identifierComponents;
    }
}

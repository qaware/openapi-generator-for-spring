package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

public class DefaultReferenceIdentifierBuilderForSchema implements ReferenceIdentifierBuilderForSchema {

    private static final String NULLABLE_IDENTIFIER_PREFIX = "nullable";
    private static final String DEPRECATED_IDENTIFIER_PREFIX = "deprecated";

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public String buildIdentifier(Schema schema, @Nullable String suggestedIdentifier, int numberOfSetters) {
        List<Object> identifierComponents = new ArrayList<>();
        if (TRUE.equals(schema.getNullable())) {
            identifierComponents.add(NULLABLE_IDENTIFIER_PREFIX);
        }
        if (TRUE.equals(schema.getDeprecated())) {
            identifierComponents.add(DEPRECATED_IDENTIFIER_PREFIX);
        }
        if (StringUtils.isNotBlank(suggestedIdentifier)) {
            identifierComponents.add(suggestedIdentifier);
        } else if (StringUtils.isNotBlank(schema.getType())) {
            identifierComponents.add(schema.getType());
            if (StringUtils.isNotBlank(schema.getFormat())) {
                identifierComponents.add(schema.getFormat());
            }
            if (schema.getItems() != null) {
                identifierComponents.add(buildIdentifier(schema.getItems(), schema.getItems().getName(), numberOfSetters));
            }
        } else {
            // fallback to dummy counted identifier, as schema are sometimes required to be referenced
            identifierComponents.addAll(Arrays.asList("Schema", counter.getAndIncrement()));
        }
        return identifierComponents.stream().map(Object::toString).collect(Collectors.joining("_"));
    }
}

package de.qaware.openapigeneratorforspring.common.reference;


import de.qaware.openapigeneratorforspring.common.schema.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultReferenceNameFactory implements ReferenceNameFactory {

    private static final String IDENTIFIER_SEPARATOR = "_";
    private final AtomicInteger schemaCounter = new AtomicInteger();

    @Override
    public ReferenceName create(Object object) {
        // TODO introduce ReferenceNameFactoryForType
        if (object instanceof Schema) {
            Schema schema = (Schema) object;
            return new ReferenceName(ReferenceName.Type.SCHEMA, getIdentifier(schema));
        }
        throw new IllegalStateException("Unknown object to create reference name for " + object.getClass());
    }

    private String getIdentifier(Schema schema) {
        if (StringUtils.isNotBlank(schema.getName())) {
            return schema.getName();
        } else if (StringUtils.isNotBlank(schema.getType())) {
            String maybeNullableType = (Boolean.TRUE.equals(schema.getNullable()) ? "nullable" + IDENTIFIER_SEPARATOR : "") + schema.getType();
            if (StringUtils.isNotBlank(schema.getFormat())) {
                return maybeNullableType + IDENTIFIER_SEPARATOR + schema.getFormat();
            }
            return maybeNullableType;
        } else {
            return "Schema_" + schemaCounter.getAndIncrement();
        }
    }
}

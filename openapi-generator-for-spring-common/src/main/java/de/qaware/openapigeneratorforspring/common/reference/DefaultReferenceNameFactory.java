package de.qaware.openapigeneratorforspring.common.reference;

import io.swagger.v3.oas.models.media.Schema;

public class DefaultReferenceNameFactory implements ReferenceNameFactory {
    @Override
    public ReferenceName create(Object object) {
        if (object instanceof Schema) {
            Schema schema = (Schema) object;
            return new ReferenceName(ReferenceName.Type.SCHEMA, schema.getName());
        }
        throw new IllegalStateException("Unknown object to create reference name for " + object.getClass());
    }
}

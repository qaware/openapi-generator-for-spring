package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.schema.Schema;

import javax.annotation.Nullable;

public class DefaultReferenceDecider implements ReferenceDecider {
    @Nullable
    @Override
    public ReferenceName decide(Schema schema, int numberOfUsages, ReferenceName uniqueReferenceName) {
        if (onlyTypeIsSet(schema)) {
            return null;
        }
        if (numberOfUsages > 1) {
            return uniqueReferenceName;
        }
        return null;
    }

    private boolean onlyTypeIsSet(Schema schema) {
        String previousType = schema.getType();
        // check if the schema becomes "empty" once the type is also null
        // this is a little trick to avoid checking all other properties!
        boolean onlyTypeIsSet = schema.type(null).isEmpty();
        schema.setType(previousType);
        return onlyTypeIsSet;
    }
}

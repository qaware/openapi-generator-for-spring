package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.schema.Schema;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ReferenceDecider {
    // TODO generalize for other things than Schema
    @Nullable
    ReferenceName decide(Schema schema, int numberOfUsages, ReferenceName uniqueReferenceName);
}

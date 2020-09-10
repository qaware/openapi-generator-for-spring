package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.schema.Schema;

@FunctionalInterface
public interface ReferenceDecider {
    // TODO generalize for other things than Schema
    boolean turnIntoReference(Schema schema, int numberOfUsages);
}

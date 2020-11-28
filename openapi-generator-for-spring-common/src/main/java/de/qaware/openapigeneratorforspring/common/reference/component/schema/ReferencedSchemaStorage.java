package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Collections;
import java.util.function.Consumer;

public class ReferencedSchemaStorage extends AbstractReferencedItemStorage<Schema> {

    ReferencedSchemaStorage(ReferenceDeciderForType<Schema> referenceDecider,
                            ReferenceIdentifierBuilderForType<Schema> referenceIdentifierFactory,
                            ReferenceIdentifierConflictResolverForType<Schema> referenceIdentifierConflictResolver) {
        super(ReferenceType.SCHEMA, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Schema::new,
                Collections.emptyList());
    }

    void storeAlwaysReference(Schema schema, Consumer<Schema> setter) {
        addEntry(schema, setter, AddEntryParameters.builder()
                .referenceRequired(true)
                .suggestedIdentifier(schema.getName())
                .build()
        );
    }

    void storeMaybeReference(Schema schema, Consumer<Schema> setter) {
        addEntry(schema, setter, AddEntryParameters.builder()
                .suggestedIdentifier(schema.getName())
                .build()
        );
    }
}

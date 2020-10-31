package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
        addEntry(schema, SchemaReferenceSetter.of(setter, true), schema.getName());
    }

    void storeMaybeReference(Schema schema, Consumer<Schema> setter) {
        addEntry(schema, SchemaReferenceSetter.of(setter, false), schema.getName());
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class SchemaReferenceSetter implements AbstractReferencedItemStorage.ReferenceSetter<Schema> {
        private final Consumer<Schema> setter;
        private final boolean referenceRequired;

        @Override
        public void consumeReference(Schema referenceItem) {
            setter.accept(referenceItem);
        }
    }
}

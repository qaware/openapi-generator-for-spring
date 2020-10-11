package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReferencedSchemaStorage extends AbstractReferencedItemStorage<Schema, ReferencedSchemaStorage.Entry> {

    ReferencedSchemaStorage(ReferenceDeciderForType<Schema> referenceDecider,
                            ReferenceIdentifierFactoryForType<Schema> referenceIdentifierFactory,
                            ReferenceIdentifierConflictResolverForType<Schema> referenceIdentifierConflictResolver) {
        super(ReferenceType.SCHEMA, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Schema::new, Entry::new);
    }

    void storeAlwaysReference(Schema schema, Consumer<Schema> setter) {
        getEntryOrAddNew(schema).addRequiredSetter(setter::accept);
    }

    void storeMaybeReference(Schema schema, Consumer<Schema> setter) {
        getEntryOrAddNew(schema).addSetter(setter::accept);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Schema> {

        private final List<ReferenceSetter<Schema>> referenceRequiredSetters = new ArrayList<>();

        protected Entry(Schema item) {
            super(item);
        }

        public void addRequiredSetter(ReferenceSetter<Schema> setter) {
            referenceRequiredSetters.add(setter);
        }

        @Override
        public boolean matches(Schema schema) {
            return Objects.equals(getItem().getName(), schema.getName()) && getItem().equals(schema);
        }

        @Override
        public boolean isReferenceRequired() {
            return !referenceRequiredSetters.isEmpty();
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return getItem().getName();
        }

        @Override
        public Stream<ReferenceSetter<Schema>> getReferenceSetters() {
            return Stream.concat(super.getReferenceSetters(), referenceRequiredSetters.stream());
        }
    }
}

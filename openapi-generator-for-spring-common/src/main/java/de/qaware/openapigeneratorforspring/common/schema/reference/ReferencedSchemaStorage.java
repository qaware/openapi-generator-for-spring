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
        super(ReferenceType.SCHEMA, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Entry::new);
    }

    void storeAlwaysReference(Schema schema, Consumer<String> setter) {
        getEntryOrAddNew(schema).addRequiredSetter(setter);
    }

    void storeMaybeReference(Schema schema, Consumer<String> setter) {
        getEntryOrAddNew(schema).addSetter(setter);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Schema> {

        private final List<Consumer<String>> referenceIdentifierRequiredSetters = new ArrayList<>();

        protected Entry(Schema item) {
            super(item);
        }

        public void addRequiredSetter(Consumer<String> setter) {
            referenceIdentifierRequiredSetters.add(setter);
        }

        @Override
        public boolean matches(Schema schema) {
            return Objects.equals(getItem().getName(), schema.getName()) && getItem().equals(schema);
        }

        @Override
        public boolean isReferenceRequired() {
            return !referenceIdentifierRequiredSetters.isEmpty();
        }

        @Override
        public int getNumberOfUsages() {
            return super.getNumberOfUsages() + referenceIdentifierRequiredSetters.size();
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return getItem().getName();
        }

        @Override
        public Stream<Consumer<String>> getReferenceIdentifierSetters() {
            return Stream.concat(super.getReferenceIdentifierSetters(), referenceIdentifierRequiredSetters.stream());
        }
    }
}

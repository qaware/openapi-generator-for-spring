package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReferencedSchemaStorage extends AbstractReferencedItemStorage<Schema, ReferencedSchemaStorage.Entry> {

    ReferencedSchemaStorage(ReferenceDeciderForType<Schema> referenceDecider,
                            ReferenceNameFactoryForType<Schema> referenceNameFactory,
                            ReferenceNameConflictResolverForType<Schema> referenceNameConflictResolver) {
        super(ReferenceName.Type.SCHEMA, referenceDecider, referenceNameFactory, referenceNameConflictResolver, Entry::new);
    }

    void storeAlwaysReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        getEntryOrAddNew(schema).addRequiredSetter(referenceNameSetter);
    }

    void storeMaybeReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        getEntryOrAddNew(schema).addSetter(referenceNameSetter);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Schema> {

        private final List<Consumer<ReferenceName>> referenceNameRequiredSetters = new ArrayList<>();

        protected Entry(Schema item) {
            super(item);
        }

        public void addRequiredSetter(Consumer<ReferenceName> referenceNameSetter) {
            referenceNameRequiredSetters.add(referenceNameSetter);
        }

        @Override
        public boolean matches(Schema schema) {
            return Objects.equals(getItem().getName(), schema.getName()) && getItem().equals(schema);
        }

        @Override
        public boolean isReferenceRequired() {
            return !referenceNameRequiredSetters.isEmpty();
        }

        @Override
        public int getNumberOfUsages() {
            return super.getNumberOfUsages() + referenceNameRequiredSetters.size();
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return getItem().getName();
        }

        @Override
        public Stream<Consumer<ReferenceName>> getReferenceNameSetters() {
            return Stream.concat(super.getReferenceNameSetters(), referenceNameRequiredSetters.stream());
        }
    }
}

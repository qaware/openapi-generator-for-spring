package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReferencedSchemaStorage extends AbstractReferencedItemStorage<Schema, ReferencedSchemaStorage.ReferencableSchemaEntry> {

    // TODO must not be public
    public ReferencedSchemaStorage(ReferenceDeciderForType<Schema> referenceDecider,
                                   ReferenceNameFactoryForType<Schema> referenceNameFactory,
                                   ReferenceNameConflictResolverForType<Schema> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, ReferencableSchemaEntry::new);
    }

    void storeSchemaAlwaysReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        getEntryOrAddNew(schema).referenceNameRequiredSetters.add(referenceNameSetter);
    }

    void storeSchemaMaybeReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        getEntryOrAddNew(schema).referenceNameOptionalSetters.add(referenceNameSetter);
    }

    @RequiredArgsConstructor
    static class ReferencableSchemaEntry implements AbstractReferencedItemStorage.ReferencableEntry<Schema> {

        private final Schema schema;
        private final List<Consumer<ReferenceName>> referenceNameRequiredSetters = new ArrayList<>();
        private final List<Consumer<ReferenceName>> referenceNameOptionalSetters = new ArrayList<>();

        @Override
        public Schema getItem() {
            return schema;
        }

        @Override
        public boolean matches(Schema schema) {
            return Objects.equals(this.schema.getName(), schema.getName()) && this.schema.equals(schema);
        }

        @Override
        public boolean isReferenceRequired() {
            return !referenceNameRequiredSetters.isEmpty();
        }

        @Override
        public int getNumberOfUsages() {
            // this method is only queried when referenceNameRequiredSetters is empty
            return referenceNameRequiredSetters.size() + referenceNameOptionalSetters.size();
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return schema.getName();
        }

        @Override
        public Stream<Consumer<ReferenceName>> getReferenceNameSetters() {
            return Stream.concat(referenceNameRequiredSetters.stream(), referenceNameOptionalSetters.stream());
        }
    }
}

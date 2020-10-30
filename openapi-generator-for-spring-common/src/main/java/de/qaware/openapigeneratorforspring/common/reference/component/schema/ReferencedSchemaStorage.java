package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
public class ReferencedSchemaStorage extends AbstractReferencedItemStorage<Schema, ReferencedSchemaStorage.Entry> {

    ReferencedSchemaStorage(ReferenceDeciderForType<Schema> referenceDecider,
                            ReferenceIdentifierFactoryForType<Schema> referenceIdentifierFactory,
                            ReferenceIdentifierConflictResolverForType<Schema> referenceIdentifierConflictResolver) {
        super(ReferenceType.SCHEMA, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Schema::new, Entry::new);
    }

    void storeAlwaysReference(Schema schema, Consumer<Schema> setter) {
        LOGGER.info("Always reference {}", schema.toPrettyString());
        getEntryOrAddNew(schema).addRequiredSetter(setter::accept);
        LOGGER.info("Done: Always reference {}", schema.toPrettyString());
    }

    void storeMaybeReference(Schema schema, Consumer<Schema> setter) {
        LOGGER.info("Maybe reference {}", schema.toPrettyString());
        getEntryOrAddNew(schema).addSetter(setter::accept);
        LOGGER.info("Done: Maybe reference {}", schema.toPrettyString());
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Schema> {

        private final List<ReferenceSetter<Schema>> referenceRequiredSetters = new ArrayList<>();

        protected Entry(Schema item) {
            super(item);
            LOGGER.info("Created new entry for {}", item.toPrettyString());
        }

        public void addRequiredSetter(ReferenceSetter<Schema> setter) {
            referenceRequiredSetters.add(setter);
        }

        @Override
        public boolean matches(Schema schema) {
            LOGGER.info("Considering entry {}", getItem().toPrettyString());
            boolean equals = getItem().equals(schema) || schema.equals(getItem());
            if (equals) {
                LOGGER.info("Found existing entry for {}", schema.toPrettyString());
            }
            return equals;
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

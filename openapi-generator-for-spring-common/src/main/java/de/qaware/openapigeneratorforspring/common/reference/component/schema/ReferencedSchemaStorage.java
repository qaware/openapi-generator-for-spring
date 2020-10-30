package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReferencedSchemaStorage extends AbstractReferencedItemStorage<Schema, ReferencedSchemaStorage.Entry> {

    ReferencedSchemaStorage(ReferenceDeciderForType<Schema> referenceDecider,
                            ReferenceIdentifierFactoryForType<Schema> referenceIdentifierFactory,
                            ReferenceIdentifierConflictResolverForType<Schema> referenceIdentifierConflictResolver) {
        super(ReferenceType.SCHEMA, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Schema::new, Entry::new);
    }

    void storeAlwaysReference(Schema schema, Consumer<Schema> setter) {
        getEntryOrAddNew(schema).addRequiredSetter(SchemaReferenceSetter.of(setter, schema.getName()));
    }

    void storeMaybeReference(Schema schema, Consumer<Schema> setter) {
        getEntryOrAddNew(schema).addSetter(SchemaReferenceSetter.of(setter, schema.getName()));
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntryWithReferenceSetter<Schema, SchemaReferenceSetter> {

        private final List<SchemaReferenceSetter> referenceRequiredSetters = new ArrayList<>();

        protected Entry(Schema item) {
            super(item);
        }

        public void addRequiredSetter(SchemaReferenceSetter setter) {
            referenceRequiredSetters.add(setter);
        }

        @Override
        public boolean isReferenceRequired() {
            return !referenceRequiredSetters.isEmpty();
        }

        @Override
        public Stream<SchemaReferenceSetter> getReferenceSetters() {
            return Stream.concat(super.getReferenceSetters(), referenceRequiredSetters.stream());
        }
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class SchemaReferenceSetter implements AbstractReferencedItemStorage.ReferenceSetter<Schema> {
        private final Consumer<Schema> setter;
        @Nullable
        @Getter
        private final String suggestedIdentifier;

        @Override
        public void consumeReference(Schema referenceItem) {
            setter.accept(referenceItem);
        }
    }
}

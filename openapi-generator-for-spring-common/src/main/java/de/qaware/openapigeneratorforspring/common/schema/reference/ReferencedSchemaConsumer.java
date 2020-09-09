package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.Value;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ReferencedSchemaConsumer {

    void maybeAsReference(Schema schema, Consumer<ReferenceName> referenceNameSetter);

    default void alwaysAsReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        alwaysAsReferences(
                Stream.of(new EntryWithSchema<Void>(null, schema)), // null entry is not used here
                referenceNames -> {
                    EntryWithReferenceName<Void> objectEntryWithReferenceName = referenceNames
                            .reduce((a, b) -> {
                                throw new IllegalStateException("Expected one element, not more than one");
                            })
                            .orElseThrow(() -> new IllegalStateException("Expected one element, not zero"));
                    referenceNameSetter.accept(objectEntryWithReferenceName.getReferenceName());
                }
        );
    }

    <T> void alwaysAsReferences(Stream<EntryWithSchema<T>> entriesWithSchemas, Consumer<Stream<EntryWithReferenceName<T>>> referenceNameSetters);

    @Value(staticConstructor = "of")
    class EntryWithSchema<T> {
        T entry;
        Schema schema;
    }

    @Value(staticConstructor = "of")
    class EntryWithReferenceName<T> {
        T entry;
        ReferenceName referenceName;
    }
}

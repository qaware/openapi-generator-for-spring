package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.Value;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ReferencedSchemaConsumer extends ReferencedItemConsumerForType<Schema, Schema> {

    default void alwaysAsReference(Schema schema, Consumer<String> setter) {
        alwaysAsReferences(
                Stream.of(new EntryWithSchema<Void>(null, schema)), // null entry is not used here
                entries -> {
                    EntryWithReferenceIdentifier<Void> entryWithReferenceIdentifier = entries
                            .reduce((a, b) -> {
                                throw new IllegalStateException("Expected one element, not more than one");
                            })
                            .orElseThrow(() -> new IllegalStateException("Expected one element, not zero"));
                    setter.accept(entryWithReferenceIdentifier.getReferenceIdentifier());
                }
        );
    }

    <T> void alwaysAsReferences(Stream<EntryWithSchema<T>> entriesWithSchemas, Consumer<Stream<EntryWithReferenceIdentifier<T>>> setters);

    @Value(staticConstructor = "of")
    class EntryWithSchema<T> {
        T entry;
        Schema schema;
    }

    @Value(staticConstructor = "of")
    class EntryWithReferenceIdentifier<T> {
        T entry;
        String referenceIdentifier;
    }
}

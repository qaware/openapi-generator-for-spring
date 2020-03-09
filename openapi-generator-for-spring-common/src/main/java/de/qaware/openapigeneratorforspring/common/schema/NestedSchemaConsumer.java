package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import lombok.Value;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface NestedSchemaConsumer {

    default void consume(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        consumeMany(
                Stream.of(new EntryWithSchema<>(null, schema)),
                referenceNames -> {
                    EntryWithReferenceName<?> objectEntryWithReferenceName = referenceNames
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Expected one element"));
                    referenceNameSetter.accept(objectEntryWithReferenceName.getReferenceName());
                }
        );
    }

    <T> void consumeMany(Stream<EntryWithSchema<T>> entriesWithSchemas, Consumer<Stream<EntryWithReferenceName<T>>> referenceNameSetters);

    @Value
    class EntryWithSchema<T> {
        T entry;
        Schema schema;
    }

    @Value
    class EntryWithReferenceName<T> {
        T entry;
        ReferenceName referenceName;
    }
}

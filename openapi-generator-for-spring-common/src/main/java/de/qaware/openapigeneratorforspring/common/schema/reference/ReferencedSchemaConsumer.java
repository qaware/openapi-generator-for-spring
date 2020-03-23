package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.Value;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ReferencedSchemaConsumer {

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

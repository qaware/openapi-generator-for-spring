package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultReferencedSchemaConsumer implements ReferencedSchemaConsumer {

    private final ReferencedSchemaStorage referencedSchemaStorage;

    @Override
    public void maybeAsReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        referencedSchemaStorage.storeSchemaMaybeReference(schema, referenceNameSetter);
    }

    @Override
    public <T> void alwaysAsReferences(Stream<EntryWithSchema<T>> entriesWithSchemasStream, Consumer<Stream<EntryWithReferenceName<T>>> referenceNameSetters) {

        List<EntryWithSchema<T>> entriesWithSchemas = entriesWithSchemasStream.collect(Collectors.toList());
        List<EntryWithReferenceName<T>> result = new ArrayList<>(Collections.nCopies(entriesWithSchemas.size(), null));

        for (int i = 0; i < entriesWithSchemas.size(); i++) {
            EntryWithSchema<T> entryWithSchema = entriesWithSchemas.get(i);
            Consumer<ReferenceName> referenceNameConsumer = new ReferenceNameConsumerForIndexedResult<>(i, result, entryWithSchema.getEntry());
            referencedSchemaStorage.storeSchemaAlwaysReference(
                    entryWithSchema.getSchema(),
                    referenceName -> {
                        referenceNameConsumer.accept(referenceName);
                        boolean allResultsAreNotNull = result.stream().noneMatch(Objects::isNull);
                        if (allResultsAreNotNull) {
                            referenceNameSetters.accept(result.stream());
                        }
                    }
            );
        }
    }

    @RequiredArgsConstructor
    private static class ReferenceNameConsumerForIndexedResult<T> implements Consumer<ReferenceName> {
        private final int i;
        private final List<EntryWithReferenceName<T>> result;
        private final T entry;

        @Override
        public void accept(ReferenceName referenceName) {
            result.set(i, EntryWithReferenceName.of(entry, referenceName));
        }
    }
}

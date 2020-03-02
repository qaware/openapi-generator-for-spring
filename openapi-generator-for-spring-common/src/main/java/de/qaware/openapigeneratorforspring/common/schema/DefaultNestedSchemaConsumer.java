package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultNestedSchemaConsumer implements NestedSchemaConsumer {

    private final ReferencedSchemaStorage referencedSchemaStorage;

    @Override
    public <T> void consumeMany(Stream<EntryWithSchema<T>> entriesWithSchemasStream, Consumer<Stream<EntryWithReferenceName<T>>> referenceNameSetters) {

        List<EntryWithSchema<T>> entriesWithSchemas = entriesWithSchemasStream.collect(Collectors.toList());

        List<EntryWithReferenceName<T>> result = new ArrayList<>();
        while (result.size() < entriesWithSchemas.size()) {
            result.add(null);
        }

        for (int i = 0; i < entriesWithSchemas.size(); i++) {
            EntryWithSchema<T> entryWithSchema = entriesWithSchemas.get(i);
            Consumer<ReferenceName> referenceNameConsumer = new ReferenceNameConsumer<>(i, result, entryWithSchema.getEntry());
            referencedSchemaStorage.storeSchema(
                    entryWithSchema.getSchema(),
                    referenceName -> {
                        referenceNameConsumer.accept(referenceName);
                        boolean allEntriesPresent = result.stream().noneMatch(Objects::isNull);
                        if (allEntriesPresent) {
                            referenceNameSetters.accept(result.stream());
                        }
                    }
            );
        }
    }

    @RequiredArgsConstructor
    private static class ReferenceNameConsumer<T> implements Consumer<ReferenceName> {
        private final int i;
        private final List<EntryWithReferenceName<T>> output;
        private final T entry;

        @Override
        public void accept(ReferenceName referenceName) {
            output.set(i, new EntryWithReferenceName<>(entry, referenceName));
        }
    }
}

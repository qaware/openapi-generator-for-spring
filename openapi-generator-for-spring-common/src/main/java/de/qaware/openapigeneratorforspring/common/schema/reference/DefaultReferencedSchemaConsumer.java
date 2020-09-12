package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultReferencedSchemaConsumer implements ReferencedSchemaConsumer {

    private final ReferencedSchemaStorage referencedSchemaStorage;

    @Override
    public void maybeAsReference(Schema schema, Consumer<Schema> schemaSetter) {
        schemaSetter.accept(schema); // immediately apply the schema, as it's not decided if the schema is referenced later at all
        referencedSchemaStorage.storeSchemaMaybeReference(schema,
                referenceName -> schemaSetter.accept(new Schema().$ref(referenceName.asReferenceString()))
        );
    }

    @Override
    public <T> void alwaysAsReferences(Stream<EntryWithSchema<T>> entriesWithSchemasStream, Consumer<Stream<EntryWithReferenceName<T>>> referenceNameSetters) {

        List<EntryWithSchema<T>> entriesWithSchemas = entriesWithSchemasStream.collect(Collectors.toList());
        List<EntryWithReferenceName<T>> result = new ArrayList<>(Collections.nCopies(entriesWithSchemas.size(), null));

        // using an IntStream here makes the index final and can then be captured in nested lambdas
        IntStream.range(0, entriesWithSchemas.size()).forEach(i -> {
            EntryWithSchema<T> entryWithSchema = entriesWithSchemas.get(i);
            referencedSchemaStorage.storeSchemaAlwaysReference(
                    entryWithSchema.getSchema(),
                    referenceName -> {
                        result.set(i, EntryWithReferenceName.of(entryWithSchema.getEntry(), referenceName));
                        // trigger once result is completely filled (= all entries are not null anymore)
                        boolean resultIsCompletelyFilled = result.stream().noneMatch(Objects::isNull);
                        if (resultIsCompletelyFilled) {
                            referenceNameSetters.accept(result.stream());
                        }
                    }
            );
        });
    }
}

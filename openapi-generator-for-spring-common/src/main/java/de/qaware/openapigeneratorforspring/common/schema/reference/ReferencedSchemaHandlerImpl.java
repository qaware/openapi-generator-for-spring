package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedSchemaHandlerImpl implements ReferencedItemHandler<Schema, Schema>, ReferencedSchemaConsumer {

    private final ReferencedSchemaStorage storage;

    @Override
    public void maybeAsReference(Schema schema, Consumer<Schema> schemaSetter) {
        schemaSetter.accept(schema); // immediately apply the schema, as it's not decided if the schema is referenced later at all
        storage.storeMaybeReference(schema,
                referenceName -> schemaSetter.accept(Schema.builder().ref(referenceName.asReferenceString()).build())
        );
    }

    @Override
    public <T> void alwaysAsReferences(Stream<EntryWithSchema<T>> entriesWithSchemasStream,
                                       Consumer<Stream<EntryWithReferenceName<T>>> referenceNameSetters) {

        List<EntryWithSchema<T>> entriesWithSchemas = entriesWithSchemasStream.collect(Collectors.toList());
        List<EntryWithReferenceName<T>> result = new ArrayList<>(Collections.nCopies(entriesWithSchemas.size(), null));

        // using an IntStream here makes the index final and can then be captured in nested lambdas
        IntStream.range(0, entriesWithSchemas.size()).forEach(i -> {
            EntryWithSchema<T> entryWithSchema = entriesWithSchemas.get(i);
            storage.storeAlwaysReference(
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

    @Override
    public void applyToComponents(Components components) {
        Map<String, Schema> referencedSchemas = storage.buildReferencedItems();
        setMapIfNotEmpty(referencedSchemas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), components::setSchemas);
    }
}

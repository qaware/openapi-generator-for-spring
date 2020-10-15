package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedSchemaHandlerImpl implements ReferencedComponentHandler, ReferencedSchemaConsumer {

    private final ReferencedSchemaStorage storage;

    @Override
    public void maybeAsReference(Schema schema, Consumer<Schema> setter) {
        // immediately apply the schema,
        // as it's not decided if the schema is referenced later at all
        setter.accept(schema);
        storage.storeMaybeReference(schema, setter);
    }

    @Override
    public void alwaysAsReference(Schema schema, Consumer<Schema> setter) {
        storage.storeAlwaysReference(schema, setter);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setSchemas);
    }
}

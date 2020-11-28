package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ReferencedSchemaHandlerImpl implements DependentReferencedComponentHandler, ReferencedSchemaConsumer {

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
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components) {
        storage.buildReferencedItems(components::setSchemas);
    }
}

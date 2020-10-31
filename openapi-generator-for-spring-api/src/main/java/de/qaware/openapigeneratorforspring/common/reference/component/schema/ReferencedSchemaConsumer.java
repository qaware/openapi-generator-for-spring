package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.function.Consumer;

public interface ReferencedSchemaConsumer extends ReferencedItemConsumerForType<Schema> {
    void alwaysAsReference(Schema schema, Consumer<Schema> setter);
}

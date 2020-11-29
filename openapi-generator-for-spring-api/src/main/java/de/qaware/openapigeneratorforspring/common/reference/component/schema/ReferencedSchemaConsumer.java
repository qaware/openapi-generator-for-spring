package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.function.Consumer;

/**
 * Consumer for to-be-referenced {@link Schema}. Also supports
 * to require a reference (useful in case of self-referencing
 * schemas).
 *
 * @see ReferencedItemConsumerForType
 */
public interface ReferencedSchemaConsumer extends ReferencedItemConsumerForType<Schema> {
    void alwaysAsReference(Schema schema, Consumer<Schema> setter);
}

package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import io.swagger.v3.oas.models.media.Schema;

@FunctionalInterface
public interface NestedSchemaConsumer {
    ReferenceName consumeAndCreateReference(Schema<?> schema);
}

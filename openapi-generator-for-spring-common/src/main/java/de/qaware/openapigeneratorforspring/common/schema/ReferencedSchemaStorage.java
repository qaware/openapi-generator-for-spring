package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;

import java.util.Map;
import java.util.function.Consumer;

public interface ReferencedSchemaStorage {
    void storeSchema(Schema schema, Consumer<ReferenceName> referenceNameConsumer);

    Map<ReferenceName, Schema> buildReferencedSchemas();
}

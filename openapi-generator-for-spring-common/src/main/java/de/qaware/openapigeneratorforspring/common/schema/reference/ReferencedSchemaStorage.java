package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.schema.Schema;

import java.util.Map;
import java.util.function.Consumer;

public interface ReferencedSchemaStorage {
    void storeSchemaMaybeReference(Schema schema, Consumer<ReferenceName> referenceNameConsumer);

    void storeSchemaAlwaysReference(Schema schema, Consumer<ReferenceName> referenceNameConsumer);

    Map<String, Schema> buildReferencedSchemas();
}

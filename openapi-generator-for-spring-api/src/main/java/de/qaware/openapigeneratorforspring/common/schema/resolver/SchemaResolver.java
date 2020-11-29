package de.qaware.openapigeneratorforspring.common.schema.resolver;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * {@link Schema} resolver including internal
 * referencing of possibly nested schemas.
 */
public interface SchemaResolver {
    /**
     * Resolve from given java type using the given
     * annotations supplier. The finally built top-level
     * schema will also be "maybe referenced" if not empty.
     *
     * @param type                     java type (Jackson type will be constructed from it)
     * @param annotationsSupplier      annotations supplier
     * @param referencedSchemaConsumer referenced schema consumer for nested schemas
     * @param schemaSetter             schema setter (consumes the result if resolved schema is not empty)
     */
    void resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer, Consumer<Schema> schemaSetter);

    /**
     * Resolve from given java class without referencing
     * the top-level schema, which is returned.
     * Annotations will be used from the given class.
     *
     * @param clazz                    java clazz (Jackson type will be constructed from it)
     * @param referencedSchemaConsumer referenced schema consumer for nested schemas
     * @return resolved schema, might be empty if input is Void.class
     */
    Schema resolveFromClassWithoutReference(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer);
}

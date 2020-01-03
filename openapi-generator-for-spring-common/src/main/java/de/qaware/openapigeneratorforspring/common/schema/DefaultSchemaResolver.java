package de.qaware.openapigeneratorforspring.common.schema;

import io.swagger.v3.oas.models.media.Schema;

public class DefaultSchemaResolver implements SchemaResolver {
    @Override
    public <T> Schema<T> resolveFromClass(Class<T> clazz) {
        Schema<T> schema = new Schema<>();
        schema.setName(clazz.getSimpleName());
        // TODO implement
        return schema;
    }
}

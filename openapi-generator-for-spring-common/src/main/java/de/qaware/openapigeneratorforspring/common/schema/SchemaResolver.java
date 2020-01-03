package de.qaware.openapigeneratorforspring.common.schema;

import io.swagger.v3.oas.models.media.Schema;

public interface SchemaResolver {
    <T> Schema<T> resolveFromClass(Class<T> clazz);
}

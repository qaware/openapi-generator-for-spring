package de.qaware.openapigeneratorforspring.common.schema.annotation;

import de.qaware.openapigeneratorforspring.common.schema.SchemaResolver;

public interface SchemaAnnotationMapperFactory {
    SchemaAnnotationMapper create(SchemaResolver schemaResolver);
}

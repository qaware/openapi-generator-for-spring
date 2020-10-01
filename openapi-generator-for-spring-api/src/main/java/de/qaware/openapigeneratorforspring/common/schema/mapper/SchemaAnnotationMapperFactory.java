package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;

public interface SchemaAnnotationMapperFactory {
    SchemaAnnotationMapper create(SchemaResolver schemaResolver);
}

package de.qaware.openapigeneratorforspring.common.schema;

public interface SchemaAnnotationMapperFactory {
    SchemaAnnotationMapper create(SchemaResolver schemaResolver);
}

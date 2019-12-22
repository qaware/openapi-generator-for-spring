package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.media.Schema;

import javax.annotation.Nullable;

public class DefaultSchemaAnnotationMapper implements SchemaAnnotationMapper {

    @Nullable
    @Override
    public Schema mapFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation) {
        return null;
    }
}

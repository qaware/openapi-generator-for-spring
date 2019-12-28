package de.qaware.openapigeneratorforspring.common.schema;

import io.swagger.v3.oas.models.media.Schema;

import javax.annotation.Nullable;

public interface SchemaAnnotationMapper {
    @Nullable
    Schema mapFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation);
}

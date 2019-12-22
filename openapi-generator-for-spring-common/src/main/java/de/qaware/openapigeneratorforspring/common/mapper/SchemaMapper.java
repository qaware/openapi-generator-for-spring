package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.media.Schema;

import javax.annotation.Nullable;

public interface SchemaMapper {
    @Nullable
    Schema fromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation);
}

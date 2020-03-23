package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;

public interface ContentAnnotationMapper {
    Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations, ReferencedSchemaConsumer referencedSchemaConsumer);

    MediaType map(io.swagger.v3.oas.annotations.media.Content contentAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}

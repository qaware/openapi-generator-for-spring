package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import io.swagger.v3.oas.models.media.Encoding;

import java.util.Map;

public interface EncodingAnnotationMapper {
    Map<String, Encoding> mapArray(io.swagger.v3.oas.annotations.media.Encoding[] encodingAnnotations, ReferencedSchemaConsumer referencedSchemaConsumer);

    Encoding map(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}

package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.media.Encoding;

import java.util.Map;

public interface EncodingAnnotationMapper {
    Map<String, Encoding> mapArray(io.swagger.v3.oas.annotations.media.Encoding[] encodingAnnotations);

    Encoding map(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation);
}

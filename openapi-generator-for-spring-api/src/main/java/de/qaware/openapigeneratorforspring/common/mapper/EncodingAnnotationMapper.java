package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.media.Encoding;

import java.util.Map;

public interface EncodingAnnotationMapper {
    Map<String, Encoding> mapArray(io.swagger.v3.oas.annotations.media.Encoding[] encodingAnnotations, MapperContext mapperContext);

    Encoding map(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation, MapperContext mapperContext);
}

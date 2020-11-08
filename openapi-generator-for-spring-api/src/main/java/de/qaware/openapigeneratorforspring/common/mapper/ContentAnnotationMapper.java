package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;

public interface ContentAnnotationMapper {
    Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations, MapperContext mapperContext);

    MediaType map(io.swagger.v3.oas.annotations.media.Content contentAnnotation, MapperContext mapperContext);
}

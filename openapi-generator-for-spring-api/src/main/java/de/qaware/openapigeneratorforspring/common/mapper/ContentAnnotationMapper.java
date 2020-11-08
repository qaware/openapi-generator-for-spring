package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;

public interface ContentAnnotationMapper {
    Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations,
                     Class<? extends HasContent> owningType, MapperContext mapperContext);

    MediaType map(io.swagger.v3.oas.annotations.media.Content contentAnnotation, MapperContext mapperContext);
}

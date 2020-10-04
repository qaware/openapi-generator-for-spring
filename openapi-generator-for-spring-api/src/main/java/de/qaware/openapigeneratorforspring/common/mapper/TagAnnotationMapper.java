package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.tag.Tag;

public interface TagAnnotationMapper {
    Tag map(io.swagger.v3.oas.annotations.tags.Tag tagAnnotation);
}

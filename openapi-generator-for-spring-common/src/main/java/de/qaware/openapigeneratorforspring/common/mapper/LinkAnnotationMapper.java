package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.links.Link;

import java.util.Map;

public interface LinkAnnotationMapper {
    Map<String, Link> mapArray(io.swagger.v3.oas.annotations.links.Link[] linkAnnotations);

    Link map(io.swagger.v3.oas.annotations.links.Link linkAnnotation);
}

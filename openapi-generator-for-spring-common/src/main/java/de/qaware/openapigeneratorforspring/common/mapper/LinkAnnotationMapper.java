package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.link.Link;

import java.util.Map;

public interface LinkAnnotationMapper {
    Map<String, Link> mapArray(io.swagger.v3.oas.annotations.links.Link[] linkAnnotations);

    Link map(io.swagger.v3.oas.annotations.links.Link linkAnnotation);
}

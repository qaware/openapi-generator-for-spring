package de.qaware.openapigeneratorforspring.common.mapper;

import java.util.Map;

public interface LinkParameterAnnotationMapper {
    Map<String, String> mapArray(io.swagger.v3.oas.annotations.links.LinkParameter[] linkParameterAnnotations);

    String map(io.swagger.v3.oas.annotations.links.LinkParameter linkAnnotation);
}

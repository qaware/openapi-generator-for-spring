package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.examples.Example;

import java.util.Map;

public interface ExampleObjectAnnotationMapper {
    Map<String, Example> mapArray(ExampleObject[] exampleObjectAnnotations);

    Example map(ExampleObject exampleObjectAnnotation);
}

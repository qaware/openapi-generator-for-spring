package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.example.Example;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.util.Map;

public interface ExampleObjectAnnotationMapper {
    Map<String, Example> mapArray(ExampleObject[] exampleObjectAnnotations);

    Example map(ExampleObject exampleObjectAnnotation);
}

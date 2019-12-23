package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.examples.Example;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;

@RequiredArgsConstructor
public class DefaultExampleObjectAnnotationMapper implements ExampleObjectAnnotationMapper {

    private final ParsableValueMapper parsableValueMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Map<String, Example> mapArray(ExampleObject[] exampleObjectAnnotations) {
        return buildMapFromArray(
                exampleObjectAnnotations,
                ExampleObject::name,
                this::map
        );
    }

    @Override
    public Example map(ExampleObject exampleObjectAnnotation) {
        Example example = new Example();
        OpenApiStringUtils.setStringIfNotBlank(exampleObjectAnnotation.summary(), example::setSummary);
        OpenApiStringUtils.setStringIfNotBlank(exampleObjectAnnotation.description(), example::setDescription);
        OpenApiStringUtils.setStringIfNotBlank(exampleObjectAnnotation.value(),
                value -> example.setValue(parsableValueMapper.parse(value))
        );
        OpenApiStringUtils.setStringIfNotBlank(exampleObjectAnnotation.externalValue(), example::setExternalValue);
        OpenApiStringUtils.setStringIfNotBlank(exampleObjectAnnotation.ref(), example::set$ref);
        OpenApiMapUtils.setMapIfNotEmpty(example::setExtensions, extensionAnnotationMapper.mapArray(exampleObjectAnnotation.extensions()));
        return example;
    }
}

package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.examples.Example;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

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
        setStringIfNotBlank(exampleObjectAnnotation.summary(), example::setSummary);
        setStringIfNotBlank(exampleObjectAnnotation.description(), example::setDescription);
        setStringIfNotBlank(exampleObjectAnnotation.value(),
                value -> example.setValue(parsableValueMapper.parse(value))
        );
        setStringIfNotBlank(exampleObjectAnnotation.externalValue(), example::setExternalValue);
        setStringIfNotBlank(exampleObjectAnnotation.ref(), example::set$ref);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(exampleObjectAnnotation.extensions()), example::setExtensions);
        return example;
    }
}

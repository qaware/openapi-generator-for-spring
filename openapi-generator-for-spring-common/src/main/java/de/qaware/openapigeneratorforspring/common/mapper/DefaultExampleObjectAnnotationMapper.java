package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.example.Example;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultExampleObjectAnnotationMapper implements ExampleObjectAnnotationMapper {

    private final ParsableValueMapper parsableValueMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public List<ExampleWithOptionalName> mapArray(ExampleObject[] exampleObjectAnnotations) {
        return Arrays.stream(exampleObjectAnnotations)
                .map(annotation -> new ExampleWithOptionalName(map(annotation), annotation.name()))
                .collect(Collectors.toList());
    }

    @Override
    public Example map(ExampleObject exampleObjectAnnotation) {
        Example example = Example.builder().build();
        setStringIfNotBlank(exampleObjectAnnotation.summary(), example::setSummary);
        setStringIfNotBlank(exampleObjectAnnotation.description(), example::setDescription);
        setStringIfNotBlank(exampleObjectAnnotation.value(),
                value -> example.setValue(parsableValueMapper.parse(value))
        );
        setStringIfNotBlank(exampleObjectAnnotation.externalValue(), example::setExternalValue);
        setStringIfNotBlank(exampleObjectAnnotation.ref(), example::setRef);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(exampleObjectAnnotation.extensions()), example::setExtensions);
        return example;
    }
}

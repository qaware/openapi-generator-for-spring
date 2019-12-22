package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultContentAnnotationMapper implements ContentAnnotationMapper {

    private final EncodingAnnotationMapper encodingAnnotationMapper;
    private final SchemaAnnotationMapper schemaAnnotationMapper;

    @Override
    public Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations) {
        Content contentFromAnnotation = new Content();
        contentFromAnnotation.putAll(
                buildMapFromArray(
                        contentAnnotations,
                        io.swagger.v3.oas.annotations.media.Content::mediaType,
                        this::map
                )
        );
        return contentFromAnnotation;
    }

    @Override
    public MediaType map(io.swagger.v3.oas.annotations.media.Content contentAnnotation) {
        MediaType mediaType = new MediaType();
        setExampleOrExamples(mediaType, contentAnnotation.examples());
        setMapIfNotEmpty(mediaType::setEncoding, encodingAnnotationMapper.mapArray(contentAnnotation.encoding()));
        mediaType.setSchema(schemaAnnotationMapper.mapFromAnnotation(contentAnnotation.schema()));
        setMapIfNotEmpty(mediaType::setExtensions, AnnotationsUtils.getExtensions(contentAnnotation.extensions()));
        return mediaType;
    }

    private void setExampleOrExamples(MediaType mediaType, ExampleObject[] examples) {
        if (examples.length == 1 && StringUtils.isBlank(examples[0].name())) {
            AnnotationsUtils.getExample(examples[0], true).ifPresent(mediaType::setExample);
        } else {
            setMapIfNotEmpty(mediaType::setExamples,
                    buildMapFromArray(examples, ExampleObject::name, AnnotationsUtils::getExample)
                            .entrySet().stream()
                            .filter(entry -> entry.getValue().isPresent())
                            .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().get()))
            );
        }
    }
}

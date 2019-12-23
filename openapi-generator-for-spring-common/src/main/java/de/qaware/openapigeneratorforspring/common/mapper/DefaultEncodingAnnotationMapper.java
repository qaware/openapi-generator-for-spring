package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import io.swagger.v3.oas.models.media.Encoding;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;


@RequiredArgsConstructor
public class DefaultEncodingAnnotationMapper implements EncodingAnnotationMapper {

    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Map<String, Encoding> mapArray(io.swagger.v3.oas.annotations.media.Encoding[] encodingAnnotations) {
        return buildMapFromArray(encodingAnnotations, io.swagger.v3.oas.annotations.media.Encoding::name, this::map);
    }

    @Override
    public Encoding map(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation) {
        Encoding encoding = new Encoding();

        OpenApiStringUtils.setStringIfNotBlank(encodingAnnotation.contentType(), encoding::setContentType);
        OpenApiStringUtils.setStringIfNotBlank(encodingAnnotation.style(), style -> encoding.setStyle(mapEncodingStyle(style)));

        if (encodingAnnotation.explode()) {
            encoding.setExplode(true);
        }
        if (encodingAnnotation.allowReserved()) {
            encoding.setAllowReserved(true);
        }

        setMapIfNotEmpty(encoding::setHeaders, headerAnnotationMapper.mapArray(encodingAnnotation.headers()));
        setMapIfNotEmpty(encoding::setExtensions, extensionAnnotationMapper.mapArray(encodingAnnotation.extensions()));

        return encoding;
    }

    private Encoding.StyleEnum mapEncodingStyle(String style) {
        return Stream.of(Encoding.StyleEnum.values())
                .filter(e -> e.toString().equals(style))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find encoding style '" + style
                        + "' in enum " + Arrays.toString(Encoding.StyleEnum.values())));
    }
}

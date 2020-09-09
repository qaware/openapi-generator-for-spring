package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import io.swagger.v3.oas.models.media.Encoding;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiEnumUtils.findEnumByToString;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;


@RequiredArgsConstructor
public class DefaultEncodingAnnotationMapper implements EncodingAnnotationMapper {

    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Map<String, Encoding> mapArray(io.swagger.v3.oas.annotations.media.Encoding[] encodingAnnotations, ReferencedSchemaConsumer referencedSchemaConsumer) {
        return buildMapFromArray(encodingAnnotations, io.swagger.v3.oas.annotations.media.Encoding::name,
                encodingAnnotation -> map(encodingAnnotation, referencedSchemaConsumer)
        );
    }

    @Override
    public Encoding map(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
        Encoding encoding = new Encoding();

        setStringIfNotBlank(encodingAnnotation.contentType(), encoding::setContentType);
        setStringIfNotBlank(encodingAnnotation.style(), style -> encoding.setStyle(findEnumByToString(style, Encoding.StyleEnum.class)));

        if (encodingAnnotation.explode()) {
            encoding.setExplode(true);
        }
        if (encodingAnnotation.allowReserved()) {
            encoding.setAllowReserved(true);
        }

        setMapIfNotEmpty(headerAnnotationMapper.mapArray(encodingAnnotation.headers(), referencedSchemaConsumer), encoding::setHeaders);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(encodingAnnotation.extensions()), encoding::setExtensions);

        return encoding;
    }
}

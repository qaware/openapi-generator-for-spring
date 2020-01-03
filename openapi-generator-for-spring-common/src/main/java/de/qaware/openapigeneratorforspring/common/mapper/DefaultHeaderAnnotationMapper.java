package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.schema.NestedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import io.swagger.v3.oas.models.headers.Header;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;

@RequiredArgsConstructor
public class DefaultHeaderAnnotationMapper implements HeaderAnnotationMapper {
    private final SchemaAnnotationMapper schemaAnnotationMapper;

    @Override
    public Map<String, Header> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, NestedSchemaConsumer nestedSchemaConsumer) {
        return buildMapFromArray(
                headerAnnotations,
                io.swagger.v3.oas.annotations.headers.Header::name,
                annotation -> map(annotation, nestedSchemaConsumer)
        );
    }

    @Override
    public Header map(io.swagger.v3.oas.annotations.headers.Header headerAnnotation, NestedSchemaConsumer nestedSchemaConsumer) {
        Header header = new Header();
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.description(), header::setDescription);
        if (headerAnnotation.deprecated()) {
            header.setDeprecated(true);
        }
        if (headerAnnotation.required()) {
            header.setRequired(true);
        }
        header.setSchema(schemaAnnotationMapper.mapFromAnnotation(headerAnnotation.schema(), nestedSchemaConsumer));
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.ref(), header::set$ref);
        return header;
    }
}

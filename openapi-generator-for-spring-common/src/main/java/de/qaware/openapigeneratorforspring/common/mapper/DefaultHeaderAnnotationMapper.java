package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import io.swagger.v3.oas.models.headers.Header;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;

@RequiredArgsConstructor
public class DefaultHeaderAnnotationMapper implements HeaderAnnotationMapper {
    private final SchemaAnnotationMapper schemaAnnotationMapper;

    @Override
    public Map<String, Header> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return buildMapFromArray(
                headerAnnotations,
                io.swagger.v3.oas.annotations.headers.Header::name,
                annotation -> map(annotation, referencedItemConsumerSupplier)
        );
    }

    @Override
    public Header map(io.swagger.v3.oas.annotations.headers.Header headerAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        Header header = new Header();
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.description(), header::setDescription);
        if (headerAnnotation.deprecated()) {
            header.setDeprecated(true);
        }
        if (headerAnnotation.required()) {
            header.setRequired(true);
        }
        ReferencedSchemaConsumer referencedSchemaConsumer = referencedItemConsumerSupplier.get(ReferencedSchemaConsumer.class);
        schemaAnnotationMapper.buildFromAnnotation(headerAnnotation.schema(), referencedSchemaConsumer, header::setSchema);
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.ref(), header::set$ref);
        return header;
    }
}

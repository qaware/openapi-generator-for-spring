package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import de.qaware.openapigeneratorforspring.model.header.Header;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultHeaderAnnotationMapper implements HeaderAnnotationMapper {
    private final SchemaAnnotationMapper schemaAnnotationMapper;

    @Override
    public List<HeaderWithOptionalName> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return Arrays.stream(headerAnnotations)
                .map(annotation -> new HeaderWithOptionalName(map(annotation, referencedItemConsumerSupplier), annotation.name()))
                .collect(Collectors.toList());
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
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.ref(), header::setRef);
        return header;
    }
}

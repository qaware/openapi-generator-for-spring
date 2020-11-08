package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import de.qaware.openapigeneratorforspring.model.header.Header;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultHeaderAnnotationMapper implements HeaderAnnotationMapper {
    private final SchemaAnnotationMapper schemaAnnotationMapper;

    @Override
    public Map<String, Header> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, MapperContext mapperContext) {
        return OpenApiMapUtils.buildStringMapFromStream(
                Arrays.stream(headerAnnotations),
                io.swagger.v3.oas.annotations.headers.Header::name,
                headerAnnotation -> map(headerAnnotation, mapperContext)
        );
    }

    @Override
    public Header map(io.swagger.v3.oas.annotations.headers.Header headerAnnotation, MapperContext referencedItemConsumerSupplier) {
        Header header = new Header();
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.description(), header::setDescription);
        if (headerAnnotation.deprecated()) {
            header.setDeprecated(true);
        }
        if (headerAnnotation.required()) {
            header.setRequired(true);
        }
        ReferencedSchemaConsumer referencedSchemaConsumer = referencedItemConsumerSupplier.getReferenceConsumer(ReferencedSchemaConsumer.class);
        setIfNotEmpty(schemaAnnotationMapper.buildFromAnnotation(headerAnnotation.schema(), referencedSchemaConsumer), schema -> {
            // when schema is not empty, we specify simple style according to spec,
            // the alternative would be to set the content property, which is not supported by @Header annotation
            header.setStyle(ParameterStyle.SIMPLE.toString());
            referencedSchemaConsumer.maybeAsReference(schema, header::setSchema);
        });

        // content is specified, but @Header annotation is missing support for it
        // example(s) are specified, but @Header annotation is missing support for it

        // TODO use ref for suggestedIdentifier
        return header;
    }
}

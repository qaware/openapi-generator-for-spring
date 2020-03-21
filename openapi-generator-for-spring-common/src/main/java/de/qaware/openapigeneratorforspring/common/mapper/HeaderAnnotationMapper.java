package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.schema.ReferencedSchemaConsumer;
import io.swagger.v3.oas.models.headers.Header;

import java.util.Map;

public interface HeaderAnnotationMapper {

    Map<String, Header> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, ReferencedSchemaConsumer referencedSchemaConsumer);

    Header map(io.swagger.v3.oas.annotations.headers.Header headerAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}

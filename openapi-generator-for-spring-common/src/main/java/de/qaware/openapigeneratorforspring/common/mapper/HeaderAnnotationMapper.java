package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.schema.NestedSchemaConsumer;
import io.swagger.v3.oas.models.headers.Header;

import java.util.Map;

public interface HeaderAnnotationMapper {

    Map<String, Header> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, NestedSchemaConsumer nestedSchemaConsumer);

    Header map(io.swagger.v3.oas.annotations.headers.Header headerAnnotation, NestedSchemaConsumer nestedSchemaConsumer);
}

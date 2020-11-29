package de.qaware.openapigeneratorforspring.common.supplier;

import com.fasterxml.jackson.databind.ObjectMapper;

@FunctionalInterface
public interface OpenApiObjectMapperSupplier {
    ObjectMapper get(Purpose purpose);

    enum Purpose {
        OPEN_API_JSON,
        SCHEMA_BUILDING,
        PARSABLE_VALUE_MAPPER
    }
}

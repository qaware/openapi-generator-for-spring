package de.qaware.openapigeneratorforspring.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Supplier;

public interface OpenApiObjectMapperSupplier extends Supplier<ObjectMapper> {
}

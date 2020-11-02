package de.qaware.openapigeneratorforspring.common.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultParsableValueMapper implements ParsableValueMapper {

    private final OpenApiObjectMapperSupplier objectMapperSupplier;

    @Override
    public Object parse(String value) {
        try {
            return objectMapperSupplier.get().readTree(value);
        } catch (JsonProcessingException e) {
            // not a JSON string, return value as is
            return value;
        }
    }
}

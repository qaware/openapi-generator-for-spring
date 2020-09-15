package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import io.swagger.v3.oas.models.parameters.RequestBody;

public interface RequestBodyAnnotationMapper {
    void applyFromAnnotation(RequestBody requestBody,
                             io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation,
                             ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}

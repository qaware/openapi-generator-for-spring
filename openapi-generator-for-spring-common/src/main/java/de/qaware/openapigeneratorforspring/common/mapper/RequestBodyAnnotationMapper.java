package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;

public interface RequestBodyAnnotationMapper {
    void applyFromAnnotation(RequestBody requestBody,
                             io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation,
                             ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}

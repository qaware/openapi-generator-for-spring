package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.media.Encoding;

import java.util.Map;

public interface EncodingAnnotationMapper {
    Map<String, Encoding> mapArray(io.swagger.v3.oas.annotations.media.Encoding[] encodingAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    Encoding map(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}

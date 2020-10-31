package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.header.Header;

import java.util.Map;

public interface HeaderAnnotationMapper {

    Map<String, Header> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    Header map(io.swagger.v3.oas.annotations.headers.Header headerAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}

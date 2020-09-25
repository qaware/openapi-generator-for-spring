package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import io.swagger.v3.oas.models.headers.Header;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

public interface HeaderAnnotationMapper {

    List<HeaderWithOptionalName> mapArray(io.swagger.v3.oas.annotations.headers.Header[] headerAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    Header map(io.swagger.v3.oas.annotations.headers.Header headerAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    @RequiredArgsConstructor
    @Getter
    class HeaderWithOptionalName {
        private final Header header;
        @Nullable
        private final String name;
    }
}

package de.qaware.openapigeneratorforspring.common.supplier;

import org.springframework.web.util.UriComponents;

public interface OpenApiBaseUriSupplier {
    UriComponents getBaseUri();
}

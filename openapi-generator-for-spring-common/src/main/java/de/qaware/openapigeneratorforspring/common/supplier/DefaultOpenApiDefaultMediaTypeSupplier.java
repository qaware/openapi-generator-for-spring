package de.qaware.openapigeneratorforspring.common.supplier;

import java.util.List;

import static java.util.Collections.singletonList;

public class DefaultOpenApiDefaultMediaTypeSupplier implements OpenApiDefaultMediaTypeSupplier {
    public static final String DEFAULT_MEDIA_TYPE_VALUE = "*/*";

    @Override
    public List<String> get(List<String> suggestedMediaTypes) {
        if(suggestedMediaTypes.isEmpty()) {
            return singletonList(DEFAULT_MEDIA_TYPE_VALUE);
        }
        return suggestedMediaTypes;
    }
}

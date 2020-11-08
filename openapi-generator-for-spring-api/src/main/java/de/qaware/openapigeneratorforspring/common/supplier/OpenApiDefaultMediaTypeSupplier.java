package de.qaware.openapigeneratorforspring.common.supplier;

import java.util.List;

public interface OpenApiDefaultMediaTypeSupplier {
    // return empty list to discard mapped media types
    List<String> get(List<String> suggestedMediaTypes);
}

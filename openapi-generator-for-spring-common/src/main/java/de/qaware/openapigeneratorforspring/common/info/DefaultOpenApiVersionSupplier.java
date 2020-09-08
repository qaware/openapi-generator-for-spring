package de.qaware.openapigeneratorforspring.common.info;

public class DefaultOpenApiVersionSupplier implements OpenApiVersionSupplier {
    @Override
    public String get() {
        // TODO maybe inspect spring boot application?
        return "unknown";
    }
}

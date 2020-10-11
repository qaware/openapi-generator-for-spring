package de.qaware.openapigeneratorforspring.common.security;

import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;

import java.util.Map;
import java.util.function.Supplier;

public interface OpenApiSecuritySchemesSupplier extends Supplier<Map<String, SecurityScheme>> {
}

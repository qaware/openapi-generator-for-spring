package de.qaware.openapigeneratorforspring.common.security;

import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Supplier for additional {@link SecurityScheme security
 * schemes}. They are added to the Open Api model.
 */
public interface OpenApiSecuritySchemesSupplier extends Supplier<Map<String, SecurityScheme>> {
}

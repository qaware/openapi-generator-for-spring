package de.qaware.openapigeneratorforspring.common.supplier;

import java.net.URI;

/**
 * Bean providing the Open Api Base URI, which works
 * even for WebFlux if called within OpenApi building.
 */
@FunctionalInterface
public interface OpenApiBaseUriSupplier {
    /**
     * Get Base URI. Might throw if called outside Open Api building.
     *
     * @return Base URI to Open Api Resource endpoint.
     */
    URI getBaseUri();
}

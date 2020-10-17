package de.qaware.openapigeneratorforspring.common.reference;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReferenceType {

    EXAMPLE("#/components/examples/"),
    HEADER("#/components/headers/"),
    PARAMETER("#/components/parameters/"),
    REQUEST_BODY("#/components/requestBodies/"),
    API_RESPONSE("#/components/responses/"),
    SCHEMA("#/components/schemas/"),
    // security schemes are referenced directly by @SecurityRequirement
    LINK("#/components/links/"),
    CALLBACK("#/components/callbacks/"),
    ;

    @Getter(AccessLevel.PROTECTED)
    private final String referencePrefix;
}

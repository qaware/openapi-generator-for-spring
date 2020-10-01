package de.qaware.openapigeneratorforspring.common.reference;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReferenceType {

    SCHEMA("#/components/schemas/"),
    API_RESPONSE("#/components/responses/"),
    PARAMETER("#/components/parameters/"),
    EXAMPLE("#/components/examples/"),
    REQUEST_BODY("#/components/requestBodies/"),
    HEADER("#/components/headers/"),
    ;

    @Getter(AccessLevel.PROTECTED)
    private final String referencePrefix;
}

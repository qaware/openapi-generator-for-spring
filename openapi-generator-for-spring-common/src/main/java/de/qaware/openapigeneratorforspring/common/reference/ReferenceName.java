package de.qaware.openapigeneratorforspring.common.reference;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Function;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ReferenceName {
    private final Type type;
    @Getter
    private final String identifier;

    public String asReferenceString() {
        return type.referencePrefix + identifier;
    }

    public ReferenceName withIdentifier(Function<String, String> identifierMapper) {
        return new ReferenceName(type, identifierMapper.apply(identifier));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Type {

        SCHEMA("#/components/schemas/"),
        API_RESPONSE("#/components/responses/"),
        PARAMETER("#/components/parameters/"),
        EXAMPLE("#/components/examples/");

        private final String referencePrefix;
    }
}

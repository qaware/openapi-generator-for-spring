package de.qaware.openapigeneratorforspring.common.reference;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Function;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ReferenceName {
    private final Type type;
    private final String identifier;

    public String asUniqueString() {
        return type.getPrefix() + identifier;
    }

    public ReferenceName withIdentifier(Function<String, String> identifierMapper) {
        return new ReferenceName(type, identifierMapper.apply(identifier));
    }

    enum Type {

        SCHEMA("#/components/schemas/");

        private final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}

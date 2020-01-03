package de.qaware.openapigeneratorforspring.common.reference;

import lombok.Value;

@Value
public class ReferenceName {
    Type type;
    String identifier;

    public String asUniqueString() {
        return type.getPrefix() + identifier;
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

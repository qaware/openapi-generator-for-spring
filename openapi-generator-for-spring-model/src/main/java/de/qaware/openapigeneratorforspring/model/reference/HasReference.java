package de.qaware.openapigeneratorforspring.model.reference;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface HasReference {
    @JsonProperty("$ref")
    String getRef();
}

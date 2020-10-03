package de.qaware.openapigeneratorforspring.model.trait;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface HasReference<T extends HasReference<?>> extends HasCreateInstance<T> {
    @JsonProperty("$ref")
    String getRef();

    void setRef(String referencePath);

    default T createReference(String referencePath) {
        T instance = createInstance();
        instance.setRef(referencePath);
        return instance;
    }
}

package de.qaware.openapigeneratorforspring.model.trait;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasIsEmpty<T> extends HasCreateInstance<T> {
    @JsonIgnore
    default boolean isEmpty() {
        return createInstance().equals(this);
    }
}

package de.qaware.openapigeneratorforspring.model.trait;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

public interface HasExtensions {
    @JsonAnyGetter
    Map<String, Object> getExtensions();
}

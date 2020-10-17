package de.qaware.openapigeneratorforspring.model.operation;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

/**
 * Callback
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#callbackObject"
 */
@EqualsAndHashCode
@ToString
public class Callback implements HasReference<Callback> {

    @JsonIgnore
    @Setter
    private String callbackUrlExpression;
    @JsonIgnore
    @Setter
    private PathItem pathItem;

    @Getter
    @Setter
    private String ref;

    @JsonAnyGetter
    public Map<String, PathItem> get() {
        return Collections.singletonMap(callbackUrlExpression, pathItem);
    }

    @Override
    public Callback createInstance() {
        return new Callback();
    }
}

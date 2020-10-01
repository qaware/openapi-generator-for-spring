package de.qaware.openapigeneratorforspring.model.requestbody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.reference.HasReference;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * RequestBody
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#requestBodyObject"
 */
@Data
@Builder
public class RequestBody implements HasExtensions, HasReference {
    private String description;
    private Content content;
    private Boolean required;
    private Map<String, Object> extensions;
    private String ref;


    @JsonIgnore
    public boolean isEmpty() {
        return builder().build().equals(this);
    }
}


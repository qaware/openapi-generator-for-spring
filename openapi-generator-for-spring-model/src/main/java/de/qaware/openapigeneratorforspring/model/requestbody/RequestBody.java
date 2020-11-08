package de.qaware.openapigeneratorforspring.model.requestbody;

import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * RequestBody
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#requestBodyObject"
 */
@Data
public class RequestBody implements HasContent, HasExtensions, HasReference<RequestBody>, HasIsEmpty<RequestBody> {
    private String description;
    private Content content;
    private Boolean required;
    private Map<String, Object> extensions;
    private String ref;

    @Override
    public RequestBody createInstance() {
        return new RequestBody();
    }
}

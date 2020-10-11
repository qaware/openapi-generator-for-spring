package de.qaware.openapigeneratorforspring.model;

import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.header.Header;
import de.qaware.openapigeneratorforspring.model.link.Link;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import de.qaware.openapigeneratorforspring.model.operation.Callback;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * Components
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#componentsObject"
 */
@Data
public class Components implements HasExtensions, HasIsEmpty<Components> {
    private Map<String, ? extends HasReference<Schema>> schemas;
    private Map<String, ? extends HasReference<ApiResponse>> responses;
    private Map<String, ? extends HasReference<Parameter>> parameters;
    private Map<String, ? extends HasReference<Example>> examples;
    private Map<String, ? extends HasReference<RequestBody>> requestBodies;
    private Map<String, ? extends HasReference<Header>> headers;
    private Map<String, ? extends HasReference<SecurityScheme>> securitySchemes;
    private Map<String, ? extends HasReference<Link>> links;
    private Map<String, ? extends HasReference<Callback>> callbacks;
    private Map<String, Object> extensions;

    @Override
    public Components createInstance() {
        return new Components();
    }
}

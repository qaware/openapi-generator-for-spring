package de.qaware.openapigeneratorforspring.model.response;

import de.qaware.openapigeneratorforspring.model.trait.HasCreateInstance;

import java.util.LinkedHashMap;

/**
 * ApiResponses
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#responsesObject"
 */
public class ApiResponses extends LinkedHashMap<String, ApiResponse> implements HasCreateInstance<ApiResponses> {

    @Override
    public ApiResponses createInstance() {
        return new ApiResponses();
    }
}


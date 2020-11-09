package de.qaware.openapigeneratorforspring.model.response;

import de.qaware.openapigeneratorforspring.model.header.Header;
import de.qaware.openapigeneratorforspring.model.link.Link;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.*;

import java.util.Map;

/**
 * ApiResponse
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#responseObject"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse implements HasContent, HasExtensions, HasReference<ApiResponse> {
    private String description;
    @Singular
    private Map<String, Header> headers;
    private Content content;
    @Singular
    private Map<String, Link> links;
    @Singular
    private Map<String, Object> extensions;
    private String ref;

    @Override
    public ApiResponse createInstance() {
        return new ApiResponse();
    }
}


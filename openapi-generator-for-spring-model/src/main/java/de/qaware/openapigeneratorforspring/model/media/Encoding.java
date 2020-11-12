package de.qaware.openapigeneratorforspring.model.media;


import de.qaware.openapigeneratorforspring.model.header.Header;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Encoding
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#encodingObject"
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Encoding implements HasExtensions {
    private String contentType;
    private Map<String, Header> headers;
    private String style;
    private Boolean explode;
    private Boolean allowReserved;
    private Map<String, Object> extensions;
}

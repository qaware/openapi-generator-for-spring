package de.qaware.openapigeneratorforspring.model.media;

import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * MediaType
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#mediaTypeObject"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MediaType implements HasExtensions, HasIsEmpty<MediaType> {
    private Schema schema;
    private Map<String, Example> examples;
    private Object example;
    private Map<String, Encoding> encoding;
    private Map<String, Object> extensions;

    @Override
    public MediaType createInstance() {
        return new MediaType();
    }
}


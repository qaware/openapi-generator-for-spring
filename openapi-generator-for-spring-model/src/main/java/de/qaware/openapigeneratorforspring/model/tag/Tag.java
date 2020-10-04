package de.qaware.openapigeneratorforspring.model.tag;


import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Map;

/**
 * Tag
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#tagObject"
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Tag implements HasExtensions, HasIsEmpty<Tag> {
    @With
    private String name;
    private String description;
    private ExternalDocumentation externalDocs;
    private Map<String, Object> extensions;

    @Override
    public Tag createInstance() {
        return new Tag();
    }
}


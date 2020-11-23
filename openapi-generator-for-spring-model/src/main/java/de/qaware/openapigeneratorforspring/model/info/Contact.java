package de.qaware.openapigeneratorforspring.model.info;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Contact
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#contactObject"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Contact implements HasExtensions, HasIsEmpty<Contact> {
    private String name;
    private String url;
    private String email;
    private Map<String, Object> extensions;

    @Override
    public Contact createInstance() {
        return new Contact();
    }
}

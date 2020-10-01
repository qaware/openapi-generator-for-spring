package de.qaware.openapigeneratorforspring.model.server;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ServerVariable
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#serverVariableObject"
 */
@Data
public class ServerVariable implements HasExtensions {
    private List<String> _enum;
    private String _default;
    private String description;
    private Map<String, Object> extensions;
}


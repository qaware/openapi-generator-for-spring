package de.qaware.openapigeneratorforspring.model.path;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * PathItem
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#pathItemObject"
 */
@Data
public class PathItem implements HasExtensions {
    private String summary;
    private String description;
    private List<Server> servers;
    private List<Parameter> parameters;
    private String $ref;

    @JsonIgnore // merged into extensions
    private Map<String, Operation> operations = new LinkedHashMap<>();
    private Map<String, Object> extensions = Collections.emptyMap();

    // jackson supports only one @JsonAnyGetter, so we merge operations and extensions here
    @Override
    public Map<String, Object> getExtensions() {
        return Stream.concat(operations.entrySet().stream(), extensions.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void addOperation(String requestMethodName, Operation operation) {
        operations.put(requestMethodName.toLowerCase(), operation);
    }
}

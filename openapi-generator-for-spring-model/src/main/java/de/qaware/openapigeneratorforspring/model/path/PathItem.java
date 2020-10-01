package de.qaware.openapigeneratorforspring.model.path;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * PathItem
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#pathItemObject"
 */
@Data
public class PathItem implements HasExtensions {
    private String summary;
    private String description;
    private Operation get;
    private Operation put;
    private Operation post;
    private Operation delete;
    private Operation options;
    private Operation head;
    private Operation patch;
    private Operation trace;
    private List<Server> servers;
    private List<Parameter> parameters;
    private String $ref;
    private Map<String, Object> extensions;
}

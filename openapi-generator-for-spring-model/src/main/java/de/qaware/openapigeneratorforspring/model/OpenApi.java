package de.qaware.openapigeneratorforspring.model;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.info.Info;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;
import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.Data;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * OpenAPI
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md"
 */
@Data
public class OpenApi implements HasExtensions {
    private String openapi = "3.0.1";
    private Info info;
    private ExternalDocumentation externalDocs;
    private List<Server> servers;
    private List<SecurityRequirement> security;
    private List<Tag> tags;
    private Paths paths;
    private Components components;
    private Map<String, Object> extensions;
}


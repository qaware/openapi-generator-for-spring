package de.qaware.openapigeneratorforspring.model.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;
import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Operation
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#operationObject"
 */
@Data
public class Operation implements HasExtensions {
    private List<String> tags;
    private String summary;
    private String description;
    private ExternalDocumentation externalDocs;
    private String operationId;
    private List<Parameter> parameters;
    private RequestBody requestBody;
    private ApiResponses responses;
    private Map<String, Callback> callbacks;
    private Boolean deprecated;
    private List<SecurityRequirement> security;
    private List<Server> servers;
    private Map<String, Object> extensions;

    @JsonIgnore
    public Optional<SecurityRequirement> getFirstSecurity() {
        return Optional.ofNullable(security)
                .map(Collection::stream)
                .flatMap(Stream::findFirst);
    }

    public void setFirstSecurity(SecurityRequirement securityRequirement) {
        if (security == null) {
            security = new ArrayList<>();
        }
        if (security.size() > 0) {
            security.set(0, securityRequirement);
        }
        security.add(securityRequirement);
    }
}


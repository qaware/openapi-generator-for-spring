package de.qaware.openapigeneratorforspring.model.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;
import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.*;

import java.util.*;
import java.util.stream.Stream;

/**
 * Operation
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#operationObject"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Operation implements HasExtensions {
    @Singular
    private List<String> tags;
    private String summary;
    private String description;
    private ExternalDocumentation externalDocs;
    private String operationId;
    @Singular
    private List<Parameter> parameters;
    private RequestBody requestBody;
    private ApiResponses responses;
    @Singular
    private Map<String, Callback> callbacks;
    private Boolean deprecated;
    @Singular("security")
    private List<SecurityRequirement> security;
    @Singular
    private List<Server> servers;
    @Singular
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
        if (!security.isEmpty()) {
            security.set(0, securityRequirement);
        }
        security.add(securityRequirement);
    }
}


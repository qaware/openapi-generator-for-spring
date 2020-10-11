package de.qaware.openapigeneratorforspring.model.server;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Server
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#serverObject"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Server implements HasExtensions, HasIsEmpty<Server> {
    private String url;
    private String description;
    private ServerVariables variables;
    private Map<String, Object> extensions;

    @Override
    public Server createInstance() {
        return new Server();
    }
}

package de.qaware.openapigeneratorforspring.test.app16;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        tags = {
                @Tag(name = "tag-1")
        },
        servers = {
                @Server(url = "http://url1", description = "Server 1")
        }
)
public class App16 {
    public static void main(String[] args) {
        SpringApplication.run(App16.class, args);
    }
}

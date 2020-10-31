package de.qaware.openapigeneratorforspring.test.app13;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        tags = @Tag(name = "tag-10")
)
public class App13 {
    public static void main(String[] args) {
        SpringApplication.run(App13.class, args);
    }
}

package de.qaware.openapigeneratorforspring.demo.webmvc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        description = "Demo API",
        version = "1.0"
))
public class WebFluxDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxDemoApplication.class, args);
    }
}

package de.qaware.openapigeneratorforspring.test.app99;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "My own title")
)
public class App99 {
    public static void main(String[] args) {
        SpringApplication.run(App99.class, args);
    }
}

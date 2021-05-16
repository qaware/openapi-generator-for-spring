package de.qaware.openapigeneratorforspring.test.app1;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "My own title")
)
class App1 {
    public static void main(String[] args) {
        SpringApplication.run(App1.class, args);
    }
}

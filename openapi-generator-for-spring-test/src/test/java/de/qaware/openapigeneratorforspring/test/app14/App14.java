package de.qaware.openapigeneratorforspring.test.app14;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Callback example",
        version = "1.0.0"
))
class App14 {
    public static void main(String[] args) {
        SpringApplication.run(App14.class, args);
    }

}

package de.qaware.openapigeneratorforspring.demo.webflux;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        description = "Demo API",
        version = "1.0"
))
@SpringBootApplication
public class DemoWebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoWebFluxApplication.class, args);
    }

}

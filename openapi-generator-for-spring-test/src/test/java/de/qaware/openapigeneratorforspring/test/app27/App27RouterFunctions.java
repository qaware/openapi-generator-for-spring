package de.qaware.openapigeneratorforspring.test.app27;

import de.qaware.openapigeneratorforspring.webflux.function.NamedHeaderPredicate;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class App27RouterFunctions {
    @Bean
    RouterFunction<ServerResponse> lambdaMapping1() {
        return route(GET("/mapping1"),
                req -> ServerResponse.ok().build()
        );
    }

    @Bean
    @Schema(implementation = String.class)
    @Tag(name = "some-tag")
    RouterFunction<ServerResponse> lambdaMapping2() {
        return route(GET("/mapping2").and(accept(MediaType.APPLICATION_JSON)),
                req -> ServerResponse.ok().build()
        );
    }

    @Bean
    @Schema(type = "string", format = "UUID")
    RouterFunction<ServerResponse> lambdaMapping3() {
        return route(GET("/mapping3").and(accept(MediaType.APPLICATION_JSON)),
                req -> ServerResponse.ok().build()
        );
    }

    @Bean
    RouterFunction<ServerResponse> lambdaMapping4() {
        return route(POST("/mapping4").and(NamedHeaderPredicate.of("header-1", "header-value")),
                req -> ServerResponse.ok().build()
        );
    }
}

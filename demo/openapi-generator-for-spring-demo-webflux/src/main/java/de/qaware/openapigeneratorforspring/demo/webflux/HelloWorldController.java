package de.qaware.openapigeneratorforspring.demo.webflux;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping(path = "hello-world")
@Tag(name = "hello-world")
public class HelloWorldController {
    @GetMapping(value = "greeting", produces = TEXT_PLAIN_VALUE)
    public Mono<String> getGreeting(@RequestParam(value = "name", required = false) @Parameter(description = "Your name") String nameMono) {
        return Mono.justOrEmpty(nameMono)
                .switchIfEmpty(Mono.just("unknown friend"))
                .map(name -> "Hello from the demo app! Nice to meet you, " + name);
    }
}

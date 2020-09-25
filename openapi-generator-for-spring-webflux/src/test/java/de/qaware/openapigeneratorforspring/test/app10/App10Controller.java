package de.qaware.openapigeneratorforspring.test.app10;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class App10Controller {
    @GetMapping("admin/mapping-1")
    public Mono<String> adminMapping1() {
        return null;
    }

    @GetMapping("user/mapping-1")
    public Flux<String> userMapping1() {
        return null;
    }
}

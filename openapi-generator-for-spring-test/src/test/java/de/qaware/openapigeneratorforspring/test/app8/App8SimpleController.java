package de.qaware.openapigeneratorforspring.test.app8;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(path = "simple")
public class App8SimpleController {

    @GetMapping("get-instant")
    public Instant getMappingReturnInstant() {
        return null;
    }

    @GetMapping("get-instant-2/{pathParam}")
    public Instant getMappingReturnInstantWithParameter(@Schema(name = "inputParameter") @RequestParam(required = false) Instant inputParameter1) {
        return null;
    }

    @GetMapping("get-response-entity-instant")
    @Schema(name = "responseEntitySchema")
    public ResponseEntity<Instant> getResponseEntityOfInstant() {
        return null;
    }
}


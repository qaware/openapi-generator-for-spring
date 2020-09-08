package de.qaware.openapigeneratorforspring.test.app8;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(path = "simple")
public class App8SimpleController {

    @GetMapping("get-instant")
    public Instant getMappingReturnString() {
        return null;
    }

    @GetMapping("get-response-entity-instant")
    public ResponseEntity<Instant> getResponseEntityOfInstant() {
        return null;
    }

}


package de.qaware.openapigeneratorforspring.test.app3;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v3")
class App3ConflictingController3 {

    @GetMapping("post-mapping")
    @Operation(operationId = "postMapping")
    public String postMappingWithExplicitOperationId() {
        return null;
    }

}


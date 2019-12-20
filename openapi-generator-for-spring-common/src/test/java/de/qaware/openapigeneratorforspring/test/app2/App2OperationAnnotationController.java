package de.qaware.openapigeneratorforspring.test.app2;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "operation-annotation")
public class App2OperationAnnotationController {

    @GetMapping("get1")
    @Operation(
            summary = "Summary 1",
            description = "Description 1",
            tags = {"TAG-1", "TAG-2", "TAG-1"}
    )
    public String getMappingOperation1() {
        return null;
    }
}


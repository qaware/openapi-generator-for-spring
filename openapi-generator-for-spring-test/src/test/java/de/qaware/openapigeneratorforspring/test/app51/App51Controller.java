package de.qaware.openapigeneratorforspring.test.app51;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
class App51Controller {

    @GetMapping
    public void mapping1(
            @Parameter(description = "Description") @RequestParam("param1") String param1,
            @Parameter(description = "Description") @RequestParam("param2") String param2

    ) {

    }

    @PutMapping
    public void mapping2(
            @Parameter(description = "Description") @RequestParam("param1") String param1,
            @Parameter(description = "Other Description") @RequestParam("param2") String param2

    ) {

    }

    @DeleteMapping
    public void mapping3(
            @Parameter(description = "Description") @RequestParam("param1") String param1,
            @Parameter(description = "Description") @RequestParam("param2") String param2,
            @Parameter(description = "Description") @RequestParam("param3") String param3
    ) {

    }

    @GetMapping("other")
    public void otherMapping1(
            @Parameter(description = "Description") @RequestParam("param1") String param1

    ) {

    }

    @PutMapping("other")
    public void otherMapping2(
            @Parameter(description = "Description") @RequestParam("param2") String param2

    ) {

    }

}

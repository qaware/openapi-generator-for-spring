package de.qaware.openapigeneratorforspring.test.app51;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.servers.Server;
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
    @Server(url = "url1", description = "Description 1")
    public void mapping1(
            @Parameter(description = "Description") @RequestParam("param1") String param1,
            @Parameter(description = "Description") @RequestParam("param2") String param2

    ) {

    }

    @PutMapping
    @Server(url = "url1", description = "Description 2")
    public void mapping2(
            @Parameter(description = "Description") @RequestParam("param1") String param1,
            @Parameter(description = "Other Description") @RequestParam("param2") String param2

    ) {

    }

    @DeleteMapping
    @Server(url = "url1", description = "Description 1")
    public void mapping3(
            @Parameter(description = "Description") @RequestParam("param1") String param1,
            @Parameter(description = "Description") @RequestParam("param2") String param2,
            @Parameter(description = "Description") @RequestParam("param3") String param3
    ) {

    }

    @GetMapping("other")
    @Server(url = "url1", description = "Description 1")
    public void otherMapping1(
            @Parameter(description = "Description") @RequestParam("param1") String param1

    ) {

    }

    @PutMapping("other")
    @Server(url = "url1", description = "Description 1")
    public void otherMapping2(
            @Parameter(description = "Description") @RequestParam("param2") String param2

    ) {

    }

}

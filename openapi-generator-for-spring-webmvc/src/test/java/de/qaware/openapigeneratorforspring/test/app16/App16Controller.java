package de.qaware.openapigeneratorforspring.test.app16;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.Servers;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Server(url = "http://url1", description = "Server 1")
@SecurityRequirement(name = "scheme1")
@RequestMapping(value = "basePath/to", consumes = MediaType.APPLICATION_JSON_VALUE)
@SecurityScheme(type = SecuritySchemeType.OAUTH2, name = "scheme4")
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "scheme3")
public class App16Controller {

    @GetMapping("/mapping1")
    @Operation(servers = @Server(url = "http://url1", description = "Server 1"))
    public void mapping1get() {

    }

    @PutMapping("/mapping1")
    @Server(url = "http://url1", description = "Server 1")
    public void mapping1put() {

    }

    @DeleteMapping("/mapping2")
    public void mapping2delete() {

    }

    @PostMapping("/mapping2")
    public void mapping2post() {

    }

    @GetMapping("/mapping3")
    @Server(url = "http://url2", description = "Server 2")
    public void mapping3() {

    }

    @GetMapping("/mapping4")
    @Servers({
            @Server(url = "http://url3", description = "Server 3"),
            @Server(url = "http://url4", description = "Server 4")
    })
    @SecurityRequirement(name = "scheme2", scopes = {"scope1", "scope2"})
    public void mapping4() {

    }
}

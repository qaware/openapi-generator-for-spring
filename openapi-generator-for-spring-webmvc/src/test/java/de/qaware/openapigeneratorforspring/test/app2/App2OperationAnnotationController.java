package de.qaware.openapigeneratorforspring.test.app2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
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

    @GetMapping("get2")
    @Operation(
            summary = "Summary 2",
            tags = {"", "   "}
    )
    public String getMappingOperation2() {
        return null;
    }

    @GetMapping("get3")
    @Operation(
            externalDocs = @ExternalDocumentation(
                    description = "external doc description",
                    url = "http://some-url",
                    extensions = {
                            @Extension(name = "extension1", properties = {
                                    @ExtensionProperty(name = "property1", value = "value1"),
                                    @ExtensionProperty(name = "property2", value = "value2")
                            })
                    }
            )
    )
    public String getMappingOperation3() {
        return null;
    }

    @GetMapping("get4")
    @Operation(
            servers = {
                    @Server(url = "http://some-other-url", description = "Server description", variables = {
                            @ServerVariable(
                                    name = "server-variable-name-1", allowableValues = {"A", "B"}, defaultValue = "A", description = "variable description",
                                    extensions = {
                                            @Extension(name = "extension2", properties = {
                                                    @ExtensionProperty(name = "property3", value = "value3"),
                                                    @ExtensionProperty(name = "property4", value = "value4")
                                            })
                                    }
                            )
                    }, extensions = {
                            @Extension(name = "extension3", properties = {
                                    @ExtensionProperty(name = "property5", value = "value5"),
                                    @ExtensionProperty(name = "property6", value = "value6")
                            })
                    })
            }
    )
    public String getMappingOperation4() {
        return null;
    }
}


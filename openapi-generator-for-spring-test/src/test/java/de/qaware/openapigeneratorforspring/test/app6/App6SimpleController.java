package de.qaware.openapigeneratorforspring.test.app6;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

@RestController
@RequestMapping(path = "simple")
class App6SimpleController {

    @GetMapping("get-mapping-1/{pathVariableName}")
    public void getMappingRequestParam1(
            @RequestParam @Nullable String someQueryStringParameterWithoutExplicitName,
            @PathVariable(name = "pathVariableName") String somePathVariableStringParameter,
            @RequestHeader(value = "headerName", defaultValue = "defaultHeaderValue") String someRequestHeaderStringParameter
    ) {
        // nothing
    }


    @Operation(
            parameters = {
                    @Parameter(name = "someStringParam2", deprecated = true),
                    @Parameter(name = "hiddenParam", hidden = true)
            }
    )
    @Parameter(name = "someStringParam1", description = "Description for String parameter")
    @GetMapping("get-mapping-2")
    public void getMappingRequestParam2(
            @RequestParam String someStringParam1,
            @RequestParam String someStringParam2,
            @Parameter(hidden = true) String someHiddenParam,
            @RequestParam("hiddenParam") String anotherHiddenParam
    ) {
        // nothing
    }
}
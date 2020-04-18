package de.qaware.openapigeneratorforspring.test.app6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

@RestController
@RequestMapping(path = "simple")
public class App6SimpleController {

    @GetMapping("get-mapping/{pathVariableName}")
    public String getMappingRequestParam(
            @RequestParam @Nullable String someQueryStringParameterWithoutExplicitName,
            @PathVariable(name = "pathVariableName") String somePathVariableStringParameter,
            @RequestHeader(value = "headerName", defaultValue = "defaultHeaderValue") String someRequestHeaderStringParameter
    ) {
        return null;
    }
}
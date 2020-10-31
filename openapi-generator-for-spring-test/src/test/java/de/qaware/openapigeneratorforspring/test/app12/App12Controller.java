package de.qaware.openapigeneratorforspring.test.app12;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static de.qaware.openapigeneratorforspring.test.app12.App12Configuration.X_FILTERED_HEADER_NAME;

@RestController
public class App12Controller {

    @GetMapping("mapping-1")
    public void mapping1(
            @RequestParam String param1,
            @RequestHeader(X_FILTERED_HEADER_NAME) String param2,
            @RequestHeader("X-Other-Header-Name") String param3,
            @RequestParam String filteredParameterName
    ) {

    }

    @GetMapping("mapping-2")
    public void mapping2(
            @RequestParam String filteredParameterName,
            @RequestHeader("filteredParameterName") String param3
    ) {

    }
}

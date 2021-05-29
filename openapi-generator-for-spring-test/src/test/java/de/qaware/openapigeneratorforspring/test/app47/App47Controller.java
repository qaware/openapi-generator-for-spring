package de.qaware.openapigeneratorforspring.test.app47;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RestController
class App47Controller {

    @GetMapping("/mapping1")
    public HashSet<String> mapping1() {
        return null;
    }

    @GetMapping("/mapping2")
    public List<String> mapping2(@RequestParam Collection<String> param1) {
        return null;
    }

    @GetMapping("/mapping3")
    @ArraySchema(uniqueItems = true)
    public List<String> mapping3(@ArraySchema(uniqueItems = true) @RequestParam Collection<String> param2) {
        return null;
    }

    @GetMapping("/mapping4")
    @ArraySchema(schema = @Schema(description = "Description for array items"), arraySchema = @Schema(description = "Description for array"))
    public List<String> mapping4() {
        return null;
    }

    @GetMapping("/mapping5")
    @ArraySchema(minItems = 2, maxItems = 5, extensions = {
            @Extension(name = "extension1", properties = {
                    @ExtensionProperty(name = "name1", value = "value1"),
                    @ExtensionProperty(name = "name2", value = "value2")
            })
    })
    public List<String> mapping5() {
        return null;
    }
}

package de.qaware.openapigeneratorforspring.test.app13;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "tag-3")
@Tag(name = "tag-8")
@Tag(name = "tag-5", description = "some description 5")
public class App13Controller {

    @GetMapping("mapping-1")
    @Tag(name = "tag-4")
    @Tag(name = "tag-8", description = "some description 8")
    public void mapping1() {

    }

    @GetMapping("mapping-2")
    @Operation(tags = {"tag-1", "tag-3"})
    @Tags({
            @Tag(name = "tag-6"),
            @Tag(name = "tag-7")
    })
    public void mapping2() {

    }

    @GetMapping("mapping-3")
    @Tag(name = "tag-5")
    @Operation(tags = {"tag-1", "tag-2"})
    public void mapping3() {

    }
}

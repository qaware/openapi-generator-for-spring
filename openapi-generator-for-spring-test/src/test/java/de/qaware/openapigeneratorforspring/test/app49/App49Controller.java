package de.qaware.openapigeneratorforspring.test.app49;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
class App49Controller {

    @GetMapping("/users{id}")
    public List<Integer> getUsers(
            @Parameter(explode = Explode.TRUE) @MatrixVariable(name = "id") @ArraySchema(minItems = 1) List<Integer> userIds,
            @RequestParam(value = "metadata", required = false) boolean metadata
    ) {
        return userIds;
    }

}

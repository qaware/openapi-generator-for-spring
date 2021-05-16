package de.qaware.openapigeneratorforspring.test.app11;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class App11Controller {

    @GetMapping("mapping-1")
    public void mapping1(
            @RequestParam @Schema(
                    discriminatorProperty = "petType",
                    discriminatorMapping = {
                            @DiscriminatorMapping(value = "dog", schema = DogDto.class),
                            @DiscriminatorMapping(value = "cat", schema = CatDto.class),
                            @DiscriminatorMapping(value = "monster", schema = MonsterDto.class),
                    }) Pet param1,
            @RequestParam MonsterDto monsterDto
    ) {

    }

    private interface Pet {

    }

    @Value
    private static class DogDto implements Pet {
        String dogProperty;
    }

    @Value
    private static class CatDto implements Pet {
        String catProperty;
    }

    @Value
    private static class MonsterDto implements Pet {
        String monsterProperty;
    }
}

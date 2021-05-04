package de.qaware.openapigeneratorforspring.common.paths;


import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpringWebRequestMethodEnumMapperTest {
    @Test
    void enumsAreBijective() {
        assertThat(Stream.of(RequestMethod.values()).map(Enum::name))
                .containsExactlyInAnyOrderElementsOf(
                        Stream.of(de.qaware.openapigeneratorforspring.model.path.RequestMethod.values())
                                .map(Enum::name)
                                .collect(Collectors.toList())
                );
    }
}
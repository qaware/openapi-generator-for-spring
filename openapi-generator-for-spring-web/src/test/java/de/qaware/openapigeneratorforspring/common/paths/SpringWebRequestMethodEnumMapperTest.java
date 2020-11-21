package de.qaware.openapigeneratorforspring.common.paths;


import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringWebRequestMethodEnumMapperTest {
    @Test
    public void enumsAreBijective() {
        assertThat(Stream.of(RequestMethod.values()).map(Enum::name))
                .containsExactlyInAnyOrder(
                        Stream.of(de.qaware.openapigeneratorforspring.model.path.RequestMethod.values())
                                .map(Enum::name)
                                .toArray(String[]::new)
                );
    }
}
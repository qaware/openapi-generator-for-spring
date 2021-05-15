package de.qaware.openapigeneratorforspring.common.schema.customizer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForJacksonPolymorphism.findCommonBaseNameStripIndex;
import static org.assertj.core.api.Assertions.assertThat;

class SchemaCustomizerForJacksonPolymorphismTest {

    @Test
    void testFindCommonBaseNameStripIndex() {
        assertThat(findCommonBaseNameStripIndex(Collections.emptyList())).isZero();
        assertThat(findCommonBaseNameStripIndex(Arrays.asList("A", "B"))).isZero();
        assertThat(findCommonBaseNameStripIndex(Arrays.asList("A.C", "B.C"))).isZero();
        assertThat(findCommonBaseNameStripIndex(Arrays.asList("A.C", "A.C"))).isEqualTo(1);
        assertThat(findCommonBaseNameStripIndex(Arrays.asList("A.C", "A.C", "A.B", "A.D"))).isEqualTo(1);
        assertThat(findCommonBaseNameStripIndex(Arrays.asList("A.B.C.D", "A.B.C.E"))).isEqualTo(5);

        assertThat(stripCommonPackagePrefix(Arrays.asList("com.foobar.Base", "com.foo.Impl")))
                .containsExactly(".foobar.Base", ".foo.Impl");
        assertThat(stripCommonPackagePrefix(Arrays.asList("com.foo.Base", "com.foo.Impl")))
                .containsExactly(".Base", ".Impl");
        assertThat(stripCommonPackagePrefix(Arrays.asList("com.foo.Base", "com.foo.impl.Impl")))
                .containsExactly(".Base", ".impl.Impl");
    }

    private static Stream<String> stripCommonPackagePrefix(Collection<String> strings) {
        int idx = findCommonBaseNameStripIndex(strings);
        return strings.stream().map(s -> s.substring(idx));
    }

}
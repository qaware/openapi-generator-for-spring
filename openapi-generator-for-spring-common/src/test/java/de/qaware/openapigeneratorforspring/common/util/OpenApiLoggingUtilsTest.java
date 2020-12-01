package de.qaware.openapigeneratorforspring.common.util;

import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenApiLoggingUtilsTest {

    @Mock
    private OpenApiLoggingUtils.HasToPrettyString hasToPrettyString;

    @BeforeAll
    static void beforeAll() {
        OpenApiLoggingUtils.registerPrettyPrinter(Schema.class, OpenApiLoggingUtils::prettyPrintSchema);
    }

    @Test
    void logPretty_schema() {
        assertThat(OpenApiLoggingUtils.logPretty(new Schema()))
                .hasToString("");
    }

    @Test
    void logPretty_derivedSchema() {
        assertThat(OpenApiLoggingUtils.logPretty(new TestSchema()))
                .hasToString("testName{prop1=,}");
    }

    @Test
    void logPretty_hasToPrettyString() {
        when(hasToPrettyString.toPrettyString()).thenReturn("pretty string");
        assertThat(OpenApiLoggingUtils.logPretty(hasToPrettyString))
                .hasToString("pretty string");
    }

    private static class TestSchema extends Schema {
        public TestSchema() {
            setName("testName");
            setType("testType");
            setProperty("prop1", Schema.builder().build());
        }
    }
}
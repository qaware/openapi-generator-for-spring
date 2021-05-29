package de.qaware.openapigeneratorforspring.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.skyscreamer.jsonassert.JSONAssert;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiJsonIntegrationTestUtils {
    private static final String JSON_PATH_PREFIX = "/openApiJson/";
    private static final String YAML_PATH_PREFIX = "/openApiYaml/";

    public static String findResourceFilenameFromTestClass(Class<?> testClass, String fileSuffix) {
        String testClassName = testClass.getSimpleName();
        if (testClassName.startsWith("App") && testClassName.endsWith("Test")) {
            String testNumber = testClassName
                    .substring(0, testClassName.length() - "Test".length())
                    .substring("App".length());
            return "app" + testNumber + fileSuffix;
        }
        throw new IllegalArgumentException("Cannot infer resource file name from " + testClassName);
    }

    public static void assertMatchesOpenApiJson(String expectedJsonFile, Callable<String> jsonSupplier) throws Exception {
        String expectedJson = readFileAsString(JSON_PATH_PREFIX + expectedJsonFile);
        String actualJson = jsonSupplier.call();
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, true);
        } catch (AssertionError e) {
            throw new AssertionError(e.getMessage() + "\n\n Actual JSON:\n" + actualJson, e);
        }
    }

    public static void assertMatchesOpenApiYaml(String expectedJsonFile, Callable<String> yamlSupplier) throws Exception {
        Yaml yaml = new Yaml();
        Map<String, Object> expectedYaml = yaml.load(readFileAsString(YAML_PATH_PREFIX + expectedJsonFile));
        String actualYamlString = yamlSupplier.call();
        Map<String, Object> actualYaml = yaml.load(actualYamlString);
        try {
            assertThat(actualYaml).isEqualTo(expectedYaml);
        } catch (AssertionError e) {
            throw new AssertionError(e.getMessage() + "\n\n Actual YAML:\n" + actualYamlString, e);
        }
    }

    private static String readFileAsString(String path) throws IOException {
        try (InputStream stream = AbstractOpenApiGeneratorWebMvcIntTest.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Unable to find resource '" + path + "'");
            }
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
    }
}

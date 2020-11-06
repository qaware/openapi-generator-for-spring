package de.qaware.openapigeneratorforspring.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiJsonIntegrationTestUtils {
    private static final String RESOURCE_PATH_PREFIX = "/openApiJson/";

    public static void assertMatchesOpenApiJson(String expectedJsonFile, Callable<String> jsonSupplier) throws Exception {
        String expectedJson = readOpenApiJsonFile(expectedJsonFile);
        String actualJson = jsonSupplier.call();
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, true);
        } catch (AssertionError e) {
            throw new AssertionError(e.getMessage() + "\n\n Actual JSON: " + actualJson, e);
        }
    }

    public static String readOpenApiJsonFile(String jsonFile) throws IOException {
        return readFileAsString(RESOURCE_PATH_PREFIX + jsonFile);
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

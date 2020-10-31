package de.qaware.openapigeneratorforspring.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiJsonFileLoader {
    private static final String RESOURCE_PATH_PREFIX = "/openApiJson/";

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

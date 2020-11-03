package de.qaware.openapigeneratorforspring.ui.swagger;

import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ResourceUtils;
import org.webjars.WebJarAssetLocator;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@RequiredArgsConstructor
public class SwaggerUiSupport {
    static final String INDEX_HTML_FILE = "index.html";
    static final String SWAGGER_UI_WEB_JAR = "swagger-ui";

    private final OpenApiSwaggerUiConfigurationProperties swaggerUiProperties;

    public String getWebJarClassPath() {
        String pathToSwaggerUiIndexHtml = new WebJarAssetLocator().getFullPath(SWAGGER_UI_WEB_JAR, INDEX_HTML_FILE);
        String pathToSwaggerUi = pathToSwaggerUiIndexHtml.substring(0, pathToSwaggerUiIndexHtml.length() - INDEX_HTML_FILE.length());
        return ResourceUtils.CLASSPATH_URL_PREFIX + pathToSwaggerUi;
    }

    public String getUiPath() {
        return swaggerUiProperties.getPath();
    }

    public String getRedirectPath() {
        return swaggerUiProperties.getPath() + DEFAULT_PATH_SEPARATOR + INDEX_HTML_FILE;
    }
}

package de.qaware.openapigeneratorforspring.autoconfigure;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenApiGeneratorWebMvcAutoConfigurationTest {

    @Mock
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Test
    void buildRequestMappingInfoOptions_defaults() {
        val builderConfiguration = OpenApiGeneratorWebMvcAutoConfiguration.buildRequestMappingInfoOptions(requestMappingHandlerMapping);
        assertThat(builderConfiguration).isNotNull();
    }

    @Test
    void buildRequestMappingInfoOptions_withPatternParser() {
        when(requestMappingHandlerMapping.getPatternParser()).thenReturn(PathPatternParser.defaultInstance);
        val builderConfiguration = OpenApiGeneratorWebMvcAutoConfiguration.buildRequestMappingInfoOptions(requestMappingHandlerMapping);
        assertThat(builderConfiguration).isNotNull();
    }
}
package de.qaware.openapigeneratorforspring.webmvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPatternParser;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerMethodPathPatternsProviderForWebMvcTest {

    private HandlerMethodPathPatternsProviderForWebMvc sut;

    @BeforeEach
    void setUp() {
        sut = new HandlerMethodPathPatternsProviderForWebMvc();
    }

    @Test
    void findPatterns() {
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths().build();
        assertThat(sut.findPatterns(requestMappingInfo)).containsExactlyInAnyOrder("");
    }

    @Test
    void findPatterns_withPatternParser() {
        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
        options.setPatternParser(PathPatternParser.defaultInstance);
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths()
                .options(options)
                .build();
        assertThat(sut.findPatterns(requestMappingInfo)).containsExactlyInAnyOrder("");
    }
}
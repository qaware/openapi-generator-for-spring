package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.parameter.OpenApiSpringWebParameterNameDiscoverer;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultSpringWebHandlerMethodBuilderTest {

    private DefaultSpringWebHandlerMethodBuilder sut;

    @Mock
    private AnnotationsSupplierFactory annotationsSupplierFactory;
    @Mock
    private OpenApiSpringWebParameterNameDiscoverer parameterNameDiscoverer;
    @Mock
    private org.springframework.web.method.HandlerMethod springWebHandlerMethod;


    @BeforeEach
    void setUp() {
        sut = new DefaultSpringWebHandlerMethodBuilder(annotationsSupplierFactory, parameterNameDiscoverer);
    }

    @Test
    void build_parameterNumberMismatch() {
        when(parameterNameDiscoverer.getParameterNames(null)).thenReturn(Arrays.asList("parameter1", "parameter2"));
        when(springWebHandlerMethod.getMethodParameters()).thenReturn(new MethodParameter[]{});

        val actual = sut.build(springWebHandlerMethod);

        assertThat(actual).isInstanceOf(SpringWebHandlerMethod.class);
    }
}
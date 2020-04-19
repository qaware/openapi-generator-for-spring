package de.qaware.openapigeneratorforspring.common.annotation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultAnnotationsSupplierFactoryTest {

    DefaultAnnotationsSupplierFactory sut = new DefaultAnnotationsSupplierFactory();

    @Test
    void plainAndCustomAnnotationWithInheritance() {
        assertThat(sut.createFromAnnotatedElement(TestClassWithPlainAnnotation.class).findAnnotations(Deprecated.class)).hasSize(1);
        assertThat(sut.createFromAnnotatedElement(InheritedTestClass.class).findAnnotations(TestAnnotation.class)).hasSize(1);
    }

    @Test
    void repeatableAnnotations() {
        assertThat(sut.createFromAnnotatedElement(TestClassWithOneRepeatable.class).findAnnotations(ApiResponse.class)).hasSize(1);
        assertThat(sut.createFromAnnotatedElement(TestClassWithOneRepeatable.class).findAnnotations(ApiResponses.class)).isEmpty();

        assertThat(sut.createFromAnnotatedElement(TestClassWithTwoRepeatable.class).findAnnotations(ApiResponse.class)).isEmpty();
        assertThat(sut.createFromAnnotatedElement(TestClassWithTwoRepeatable.class).findAnnotations(ApiResponses.class))
                .hasSize(1)
                .allSatisfy(apiResponses -> assertThat(apiResponses.value()).hasSize(2));
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
    public @interface TestAnnotation {
        // just for testing
    }

    @SuppressWarnings("DeprecatedIsStillUsed") // annotation used for test
    @Deprecated
    @TestAnnotation
    private static class TestClassWithPlainAnnotation {
        // just for testing
    }

    private static class InheritedTestClass extends TestClassWithPlainAnnotation {
        // just for testing
    }

    @ApiResponse(description = "description1")
    @ApiResponse(description = "description2")
    private static class TestClassWithTwoRepeatable {
        // just for testing
    }

    @ApiResponse(description = "description1")
    private static class TestClassWithOneRepeatable {
        // just for testing
    }
}
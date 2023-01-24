package de.qaware.openapigeneratorforspring.common.annotation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
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

    private DefaultAnnotationsSupplierFactory sut;

    @BeforeEach
    void setUp() throws Exception {
        sut = new DefaultAnnotationsSupplierFactory();
    }

    @Test
    void plainAndCustomAnnotationWithInheritance() {
        assertThat(sut.createFromAnnotatedElement(TestClassWithPlainAnnotation.class).findAnnotations(Deprecated.class)).hasSize(1);
        assertThat(sut.createFromAnnotatedElement(InheritedTestClass.class).findAnnotations(TestAnnotation.class)).hasSize(1);
    }

    @Test
    void repeatableAnnotations() {
        assertThat(sut.createFromAnnotatedElement(TestClassWithOneRepeatable.class).findAnnotations(RepeatableTestAnnotation.class)).hasSize(1);
        assertThat(sut.createFromAnnotatedElement(TestClassWithOneRepeatable.class).findAnnotations(RepeatableTestAnnotations.class)).isEmpty();

        assertThat(sut.createFromAnnotatedElement(TestClassWithTwoRepeatable.class).findAnnotations(RepeatableTestAnnotation.class)).hasSize(2);
        assertThat(sut.createFromAnnotatedElement(TestClassWithTwoRepeatable.class).findAnnotations(RepeatableTestAnnotations.class)).isEmpty();

        assertThat(sut.createFromAnnotatedElement(TestClassWithRepeatableTestAnnotations.class).findAnnotations(RepeatableTestAnnotation.class)).hasSize(2);
        assertThat(sut.createFromAnnotatedElement(TestClassWithRepeatableTestAnnotations.class).findAnnotations(RepeatableTestAnnotations.class)).isEmpty();
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
    private @interface TestAnnotation {
        // just for testing
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
    @Repeatable(RepeatableTestAnnotations.class)
    private @interface RepeatableTestAnnotation {
        // just for testing
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
    private @interface RepeatableTestAnnotations {
        // just for testing
        RepeatableTestAnnotation[] value() default {};

        // this otherProperty makes it appear as a stand-alone annotation,
        // otherwise, Spring annotations support filters it
        boolean otherProperty() default false;
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

    @RepeatableTestAnnotation
    @RepeatableTestAnnotation
    private static class TestClassWithTwoRepeatable {
        // just for testing
    }

    @RepeatableTestAnnotations({@RepeatableTestAnnotation, @RepeatableTestAnnotation})
    private static class TestClassWithRepeatableTestAnnotations {
        // just for testing
    }

    @RepeatableTestAnnotation
    private static class TestClassWithOneRepeatable {
        // just for testing
    }

}

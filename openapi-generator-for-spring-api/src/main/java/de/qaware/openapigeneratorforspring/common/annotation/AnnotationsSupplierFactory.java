package de.qaware.openapigeneratorforspring.common.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Factory for {@link AnnotationsSupplier}. Can be autowired as bean.
 */
public interface AnnotationsSupplierFactory {
    /**
     * Create annotations supplier from annotated element.
     * Prefer {@link #createFromMember} if possible.
     *
     * @param annotatedElement annotated element
     * @return annotations supplier
     */
    AnnotationsSupplier createFromAnnotatedElement(AnnotatedElement annotatedElement);

    /**
     * Create from Jackson annotated member.
     * Preferred over {@link #createFromAnnotatedElement} if possible.
     *
     * @param annotatedMember Jackson's annotated member
     * @return annotations supplier
     */
    AnnotationsSupplier createFromMember(AnnotatedMember annotatedMember);

    /**
     * Helper method to conveniently find annotations on {@link Method} including its declaring class.
     *
     * @param method method
     * @return annotations supplier
     */
    default AnnotationsSupplier createFromMethodWithDeclaringClass(Method method) {
        return createFromAnnotatedElement(method)
                .andThen(createFromAnnotatedElement(method.getDeclaringClass()));
    }
}

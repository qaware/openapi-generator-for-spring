package de.qaware.openapigeneratorforspring.common.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public interface AnnotationsSupplierFactory {
    AnnotationsSupplier createFromMember(AnnotatedMember annotatedMember);

    AnnotationsSupplier createFromAnnotatedElement(AnnotatedElement annotatedElement);

    default AnnotationsSupplier createFromMethodWithDeclaringClass(Method method) {
        return createFromAnnotatedElement(method)
                .andThen(createFromAnnotatedElement(method.getDeclaringClass()));
    }
}

package de.qaware.openapigeneratorforspring.common.util;


import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class OpenApiAnnotationUtils {
    private OpenApiAnnotationUtils() {
        // static methods only
    }

    @Nullable
    public static <A extends Annotation> A findAnnotationOnMethodOrClass(Method method, Class<A> annotationClass) {
        A annotationOnMethod = AnnotationUtils.findAnnotation(method, annotationClass);
        if (annotationOnMethod != null) {
            return annotationOnMethod;
        }
        return AnnotationUtils.findAnnotation(method.getDeclaringClass(), annotationClass);
    }
}

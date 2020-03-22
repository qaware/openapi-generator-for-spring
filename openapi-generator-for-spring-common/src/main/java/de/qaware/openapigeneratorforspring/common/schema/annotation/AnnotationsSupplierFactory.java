package de.qaware.openapigeneratorforspring.common.schema.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public interface AnnotationsSupplierFactory {
    AnnotationsSupplier createForMember(AnnotatedMember annotatedMember);

    AnnotationsSupplier createForClass(Class<?> clazz);
}

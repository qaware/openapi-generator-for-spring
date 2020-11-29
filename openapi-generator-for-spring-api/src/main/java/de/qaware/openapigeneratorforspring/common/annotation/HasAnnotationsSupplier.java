package de.qaware.openapigeneratorforspring.common.annotation;

/**
 * Trait for things having an {@link AnnotationsSupplier}.
 * See also {@link AnnotationsSupplier#merge}.
 */
public interface HasAnnotationsSupplier {
    AnnotationsSupplier getAnnotationsSupplier();
}

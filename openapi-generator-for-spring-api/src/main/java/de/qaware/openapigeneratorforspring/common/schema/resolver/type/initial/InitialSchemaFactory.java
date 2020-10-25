package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface InitialSchemaFactory extends Ordered {
    int DEFAULT_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    @Nullable
    InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialTypeResolver fallbackResolver);
}

package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

public interface SimpleTypeResolver extends Ordered {
    @Nullable
    Schema resolveFromType(JavaType javaType);
}

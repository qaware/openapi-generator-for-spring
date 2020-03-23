package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface SimpleTypeResolver extends Ordered {

    int DEFAULT_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    @Nullable
    Schema resolveFromType(JavaType javaType);
}

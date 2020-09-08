package de.qaware.openapigeneratorforspring.test.app8;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.AbstractTypeResolver;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.Instant;

@Component
public class InstantTypeResolver extends AbstractTypeResolver {

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Nullable
    @Override
    protected Schema resolveSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        if (javaType.getRawClass().equals(Instant.class)) {
            return new Schema().type("string").format("ISO8601");
        }
        return null;
    }
}

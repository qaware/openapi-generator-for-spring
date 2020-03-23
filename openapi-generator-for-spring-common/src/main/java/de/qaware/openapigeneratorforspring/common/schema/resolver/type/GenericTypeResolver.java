package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import org.springframework.core.Ordered;

import java.util.function.Consumer;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface GenericTypeResolver extends Ordered {

    int DEFAULT_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer);
}

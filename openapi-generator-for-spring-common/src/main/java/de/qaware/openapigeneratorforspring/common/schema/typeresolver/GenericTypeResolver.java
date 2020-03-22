package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.annotation.AnnotationsSupplier;
import org.springframework.core.Ordered;

import java.util.function.Consumer;

public interface GenericTypeResolver extends Ordered {
    boolean apply(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer);
}

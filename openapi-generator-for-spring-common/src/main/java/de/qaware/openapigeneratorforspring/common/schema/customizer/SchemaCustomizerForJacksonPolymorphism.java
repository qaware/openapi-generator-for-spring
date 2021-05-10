/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 - 2021 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Discriminator;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

// TODO: CMÜ TEST + javadoc
@Slf4j
public class SchemaCustomizerForJacksonPolymorphism implements SchemaCustomizer {

    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaResolver schemaResolver, SchemaResolver.Mode mode, ReferencedSchemaConsumer referencedSchemaConsumer) {

        annotationsSupplier.findAnnotations(JsonTypeInfo.class)
                .findFirst()
                .ifPresent(typeInfo ->
                        schema.setDiscriminator(Discriminator.builder().propertyName(typeInfo.property()).build()));

        annotationsSupplier.findAnnotations(JsonSubTypes.class).findFirst().ifPresent(subTypes -> {
            if (schema.getDiscriminator() == null) {
                LOGGER.debug("Without JsonTypeInfo, we have no discriminator name. So we cannot use a mapping.");
                return;
            }
            if (schema.getDiscriminator().getMapping() == null) {
                schema.getDiscriminator().setMapping(new HashMap<>());
            }
            for (JsonSubTypes.Type type : subTypes.value()) {
                Schema resolvedSchema = schemaResolver.resolveFromClassWithoutReference(mode, type.value(), referencedSchemaConsumer);
                schema.getDiscriminator().getMapping().put(type.name(), resolvedSchema.getRef());
            }
        });
    }
}

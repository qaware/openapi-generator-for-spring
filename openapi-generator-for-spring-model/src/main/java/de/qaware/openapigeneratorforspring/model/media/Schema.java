package de.qaware.openapigeneratorforspring.model.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Schema
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#schemaObject"
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Schema implements HasExtensions, HasReference<Schema>, HasIsEmpty<Schema> {
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private String name;

    @JsonProperty("default")
    private Object _default;
    private String title;
    private BigDecimal multipleOf;
    private BigDecimal maximum;
    private Boolean exclusiveMaximum;
    private BigDecimal minimum;
    private Boolean exclusiveMinimum;
    private Integer maxLength;
    private Integer minLength;
    private String pattern;
    private Integer maxItems;
    private Integer minItems;
    private Boolean uniqueItems;
    private Integer maxProperties;
    private Integer minProperties;
    private List<String> required;
    @With
    private String type;
    private Schema not;
    private Map<String, Schema> properties;
    private Object additionalProperties;
    private String description;
    private String format;
    private String ref;
    private Boolean nullable;
    private Boolean readOnly;
    private Boolean writeOnly;
    private Object example;
    private ExternalDocumentation externalDocs;
    private Boolean deprecated;
    private XML xml;
    @JsonProperty("enum")
    private List<Object> _enum;
    private Discriminator discriminator;
    private Schema items;
    private Map<String, Object> extensions;

    public void setProperty(String propertyName, Schema propertySchema) {
        if (properties == null) {
            properties = new LinkedHashMap<>();
        }
        properties.put(propertyName, propertySchema);
    }

    public void addRequired(String requiredProperty) {
        if (required == null) {
            required = new ArrayList<>();
        }
        required.add(requiredProperty);
    }

    @Override
    public Schema createInstance() {
        return new Schema();
    }

    public String toPrettyString() {
        return toPrettyString(true);
    }

    public String toPrettyString(boolean includeProperties) {
        StringBuilder sb = new StringBuilder();
        if (nullable != null && nullable) {
            sb.append('?');
        }
        if (name != null) {
            sb.append(name);
        } else if (items != null) {
            sb.append('[').append(items.toPrettyString()).append(']');
        } else if (type != null) {
            sb.append("type:").append(type);
        } else if (ref != null) {
            sb.append("-->").append(ref);
        }

        if (includeProperties && properties != null) {
            sb.append('{');
            properties.forEach((propertyName, propertySchema) -> {
                sb.append(propertyName);
                sb.append('=');
                sb.append(propertySchema.toPrettyString());
                sb.append(',');
            });
            sb.append("}");
        }
        return sb.toString();
    }

}


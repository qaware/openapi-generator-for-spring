package de.qaware.openapigeneratorforspring.common.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "squid:S2160"})
@Getter
@Setter
public class Schema extends io.swagger.v3.oas.models.media.Schema {
    private Schema items = null;

    @Override
    public Schema $ref(String reference) {
        set$ref(reference);
        return this;
    }

    @Override
    public Schema type(String type) {
        setType(type);
        return this;
    }

    @Override
    public Schema format(String format) {
        setFormat(format);
        return this;
    }

    @Override
    @JsonIgnore
    public boolean getExampleSetFlag() {
        return super.getExampleSetFlag();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return new Schema().equals(this);
    }
}

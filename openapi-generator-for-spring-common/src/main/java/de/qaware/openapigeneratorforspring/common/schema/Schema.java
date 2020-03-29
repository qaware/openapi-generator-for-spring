package de.qaware.openapigeneratorforspring.common.schema;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "squid:S2160"})
@Getter
@Setter
public class Schema extends io.swagger.v3.oas.models.media.Schema {
    private Schema items = null;

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
}

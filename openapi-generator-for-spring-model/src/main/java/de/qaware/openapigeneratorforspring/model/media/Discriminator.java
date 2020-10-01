package de.qaware.openapigeneratorforspring.model.media;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Discriminator {
    private String propertyName;
    private Map<String, String> mapping;
}

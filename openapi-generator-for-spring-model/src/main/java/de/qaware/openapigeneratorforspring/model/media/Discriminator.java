package de.qaware.openapigeneratorforspring.model.media;

import lombok.Data;

import java.util.Map;

@Data
public class Discriminator {
    private String propertyName;
    private Map<String, String> mapping;
}

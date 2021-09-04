package de.qaware.openapigeneratorforspring.test.app50;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class HeaderDtoConverter implements Converter<String, App50Controller.HeaderDto> {

    @Override
    public App50Controller.HeaderDto convert(String source) {
        String[] split = source.split(";");
        if (split.length != 2) {
            return App50Controller.HeaderDto.fromParams("unknown", "unknown");
        }
        return App50Controller.HeaderDto.fromParams(split[0], split[1]);
    }
}

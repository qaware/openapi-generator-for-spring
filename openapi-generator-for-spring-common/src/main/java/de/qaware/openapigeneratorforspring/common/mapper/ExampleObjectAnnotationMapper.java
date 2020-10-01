package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.example.Example;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

public interface ExampleObjectAnnotationMapper {
    List<ExampleWithOptionalName> mapArray(ExampleObject[] exampleObjectAnnotations);

    Example map(ExampleObject exampleObjectAnnotation);

    @RequiredArgsConstructor
    @Getter
    class ExampleWithOptionalName {
        private final Example example;
        @Nullable
        private final String name;
    }
}

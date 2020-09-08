package de.qaware.openapigeneratorforspring.common.mapper;


import io.swagger.v3.oas.models.info.Info;

import javax.annotation.Nullable;

public interface InfoAnnotationMapper {
    @Nullable
    Info map(io.swagger.v3.oas.annotations.info.Info infoAnnotation);
}
